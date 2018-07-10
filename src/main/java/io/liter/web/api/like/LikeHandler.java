package io.liter.web.api.like;

import io.liter.web.api.like.view.LikeList;
import io.liter.web.api.review.ReviewRepository;
import io.liter.web.api.review.view.Pagination;
import io.liter.web.api.review.view.ReviewDetail;
import io.liter.web.api.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

import static org.springframework.web.reactive.function.server.ServerResponse.*;

@Slf4j
@Component
public class LikeHandler {


    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReviewRepository reviewRepository;

    public LikeHandler(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }


    /**
     * GET a Single Object by ID
     **/
    public Mono<ServerResponse> getById(ServerRequest request) {


        log.info("]-----] LikeHandler::get a Single Object call [-----[ ");

        LikeList likeList = new LikeList();
        Pagination paginations = new Pagination();

        Integer page = request.queryParam("page").isPresent() ? Integer.parseInt(request.queryParam("page").get()) : 0 ;
        Integer size = request.queryParam("size").isPresent() ? Integer.parseInt(request.queryParam("size").get()) : 10 ;

        ObjectId reviewId = new ObjectId((request.pathVariable("reviewId")));

        log.info("]-----] page [-----[ {}", page);
        log.info("]-----] size [-----[ {}", size);


        return request.principal()
                .flatMap(p -> this.userRepository.findByUsername(p.getName()))
                .flatMap(user -> this.likeRepository.findByReviewId(reviewId))
                .flatMap(liker -> this.userRepository.findAllById(liker.getLikeId())
                        .collectList()
                        .map(col -> {
                            //todo::팔로워가 나를 팔로잉하고 있는지 확인

                            likeList.setLikeUser(col);
                            return liker;
                        }))
                .flatMap(li -> this.userRepository.countByIdIn(li.getLikeId()))
                .flatMap(count -> {
                    paginations.setTotal(count);
                    paginations.setPage(page);
                    paginations.setSize(size);

                    likeList.setPagination(paginations);
                    return ok().body(BodyInserters.fromObject(likeList));
                })
        .switchIfEmpty(notFound().build());
    }


    /**
     * POST a Object
     */

    public Mono<ServerResponse> post(ServerRequest request) {
        log.info("]-----] LikeHandler::post call [-----[ ");
        Like like = new Like();

        ObjectId reviewId = new ObjectId(request.pathVariable("reviewId"));

        return request
                .principal()
                .map(p -> p.getName())
                .flatMap(user -> this.userRepository.findByUsername(user))
                .flatMap(user -> {

                    ArrayList<ObjectId> likes = new ArrayList<>();
                    likes.add(user.getId());
                    like.setLikeId(likes);
                    like.setReviewId((reviewId));


                    return ServerResponse.ok().body(this.likeRepository.save(like), Like.class);
                })
                .switchIfEmpty(notFound().build());
    }


    /**
     * DELETE a Object
     */

    public Mono<ServerResponse> delete(ServerRequest request) {
        log.info("]-----] ReviewHandler::delete call [-----[ ");

        return this.likeRepository.findById(new ObjectId(request.pathVariable("id")))
                .flatMap((post) -> noContent().build())
                .switchIfEmpty(notFound().build());
    }
}
