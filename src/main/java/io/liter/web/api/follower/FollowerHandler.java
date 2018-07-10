package io.liter.web.api.follower;

import io.liter.web.api.follower.view.FollowerList;
import io.liter.web.api.review.view.Pagination;
import io.liter.web.api.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Objects;

import static org.springframework.web.reactive.function.server.ServerResponse.notFound;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Slf4j
@Component
public class FollowerHandler {

    //todo::getAllMyFollower        ->  findAll
    //todo::getAllUserIdFollower    ->  findAllByUserId
    //todo::post                    ->  post
    //todo::put                     ->  put

    private final UserRepository userRepository;

    private final FollowerRepository followerRepository;

    public FollowerHandler(
            UserRepository userRepository
            , FollowerRepository followerRepository
    ) {
        this.userRepository = userRepository;
        this.followerRepository = followerRepository;
    }

    /**
     * test002 -> test001, test003
     */

    /**
     * GET All Follower by MyId
     */
    public Mono<ServerResponse> findAll(ServerRequest request) {
        log.info("]-----] FollowerHandler::findAll call [-----[ ");

        /**
         * 나를 팔로워 하는 사람들
         */

        FollowerList followerList = new FollowerList();
        Pagination pagination = new Pagination();

        Integer page = request.queryParam("page").isPresent() ? Integer.parseInt(request.queryParam("page").get()) : 0 ;
        Integer size = request.queryParam("size").isPresent() ? Integer.parseInt(request.queryParam("size").get()) : 10 ;

        log.info("]-----] page [-----[ {}", page);
        log.info("]-----] size [-----[ {}", size);


        return request.principal()
                .flatMap(p -> this.userRepository.findByUsername(p.getName()))
                .flatMap(user -> this.followerRepository.findByUserId(user.getId()))
                .flatMap(follower -> this.userRepository.findAllById(follower.getFollowerId())
                        .collectList()
                        .map(collections -> {
                            //todo::팔로워가 나를 팔로잉하고 있는지 확인

                            followerList.setFollowerUser(collections);
                            return follower;
                        }))
                .flatMap(follower -> this.userRepository.countByIdIn(follower.getFollowerId()))
                .flatMap(count -> {
                    pagination.setTotal(count);
                    pagination.setPage(page);
                    pagination.setSize(size);

                    followerList.setPagination(pagination);

                    return ok().body(BodyInserters.fromObject(followerList));
                })
                .switchIfEmpty(notFound().build());
    }

    /**
     * GET All Follower by UserId
     */
    public Mono<ServerResponse> findAllByUserId(ServerRequest request) {
        log.info("]-----] FollowerHandler::findAllByUserId call [-----[ ");

        /**
         * 특정 유저를 팔로워 하는 사람들
         */

        FollowerList followerList = new FollowerList();
        Pagination pagination = new Pagination();

        Integer page = request.queryParam("page").isPresent() ? Integer.parseInt(request.queryParam("page").get()) : 0 ;
        Integer size = request.queryParam("size").isPresent() ? Integer.parseInt(request.queryParam("size").get()) : 10 ;

        ObjectId userId = new ObjectId(request.pathVariable("userId"));

        return request.principal()
                .flatMap(p -> this.userRepository.findByUsername(p.getName()))
                .flatMap(user -> this.followerRepository.findByUserId(userId))
                .flatMap(follower -> this.userRepository.findAllById(follower.getFollowerId())
                        .collectList()
                        .map(collections -> {
                            followerList.setFollowerUser(collections);
                            return follower;
                        }))
                .flatMap(follower -> this.userRepository.countByIdIn(follower.getFollowerId()))
                .flatMap(count -> {
                    pagination.setTotal(count);
                    pagination.setPage(page);
                    pagination.setSize(size);

                    followerList.setPagination(pagination);

                    return ok().body(BodyInserters.fromObject(followerList));
                })
                .switchIfEmpty(notFound().build());
    }

    /**
     * POST a Follower
     */
    public Mono<ServerResponse> post(ServerRequest request) {
        log.info("]-----] FollowerHandler::post call [-----[ ");

        Follower follower = new Follower();
        ObjectId userId = new ObjectId(request.pathVariable("userId"));

        return request.principal()
                .flatMap(p -> this.userRepository.findByUsername(p.getName()))
                .flatMap(following -> {
                    ArrayList<ObjectId> followerList = new ArrayList<>();

                    followerList.add(following.getId());

                    follower.setFollowerId(followerList);
                    follower.setUserId(userId);

                    return ok().body(this.followerRepository.save(follower), Follower.class);
                })
                .switchIfEmpty(notFound().build());
    }

    /**
     * PUT a Follower
     */
    public Mono<ServerResponse> put(ServerRequest request) {
        log.info("]-----] FollowerHandler::pust call [-----[ ");

        ObjectId userId = new ObjectId(request.pathVariable("userId"));

        /*return request.principal()
                .map(p -> p.getName())
                .flatMap(username -> this.userRepository.findByUsername(username)
                        .filter(user -> (Objects.equals(user.getId(), userId) == false)))
                .flatMap()*/


        return ok().build();


    }
}