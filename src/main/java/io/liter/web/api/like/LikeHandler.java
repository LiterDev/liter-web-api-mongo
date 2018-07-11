package io.liter.web.api.like;

import io.liter.web.api.like.view.LikeList;
import io.liter.web.api.review.ReviewRepository;
import io.liter.web.api.review.view.Pagination;
import io.liter.web.api.review.view.ReviewDetail;
import io.liter.web.api.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Objects;

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

    private final ReactiveMongoTemplate mongoTemplate;

    public LikeHandler(LikeRepository likeRepository
            , ReactiveMongoTemplate mongoTemplate) {
        this.likeRepository = likeRepository;
        this.mongoTemplate = mongoTemplate;


    }


    /**
     * GET a Single Object by ID
     **/
    public Mono<ServerResponse> getById(ServerRequest request) {


        log.info("]-----] LikeHandler::get a Single Object call [-----[ ");

        LikeList likeList = new LikeList();
        Pagination paginations = new Pagination();

        Integer page = request.queryParam("page").isPresent() ? Integer.parseInt(request.queryParam("page").get()) : 0;
        Integer size = request.queryParam("size").isPresent() ? Integer.parseInt(request.queryParam("size").get()) : 10;

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

                            /** 1.좋아요 테이블에서 팔로우 여부 가져오기
                             * 2.like list**/

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
        Query query = new Query();
        Update update = new Update();

        ObjectId reviewId = new ObjectId(request.pathVariable("reviewId"));

        return request
                .principal()
                .flatMap(p -> this.userRepository.findByUsername(p.getName()))
                .filter(user -> Objects.equals(reviewId, user.getId()) == false)
                .doOnNext(user -> query.addCriteria(Criteria.where("userId").is(reviewId)))
                .doOnNext(user -> update.addToSet("LikeId", user.getId()))
                .flatMap(user -> mongoTemplate.upsert(query, update, Like.class))
                .doOnNext(f -> log.debug("]-----] getMatchedCount [-----[ {}", f.getMatchedCount()))
                .doOnNext(f -> log.debug("]-----] getModifiedCount [-----[ {}", f.getModifiedCount()))
                .doOnNext(f -> log.debug("]-----] wasAcknowledged [-----[ {}", f.wasAcknowledged()))
                .doOnNext(f -> log.debug("]-----] getClass [-----[ {}", f.getClass()))


                .flatMap(r -> ok().build())
                .switchIfEmpty(notFound().build());
          /*   .flatMap(user -> {

                       ArrayList<ObjectId> likes = new ArrayList<>();
                       likes.add(user.getId());
                       like.setLikeId(likes);
                       like.setReviewId((reviewId));


                       return ServerResponse.ok().body(this.likeRepository.save(like), Like.class);
                   })*/
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
