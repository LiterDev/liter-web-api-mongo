package io.liter.web.api.follower;

import io.liter.web.api.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

import static org.springframework.web.reactive.function.server.ServerResponse.notFound;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Slf4j
@Component
public class FollowerHandler {


    private final UserRepository userRepository;

    private final FollowerRepository followerRepository;


    public FollowerHandler(
            UserRepository userRepository
            , FollowerRepository followerRepository
    ) {
        this.userRepository = userRepository;
        this.followerRepository = followerRepository;
    }

    public Mono<ServerResponse> findAllByUserId (ServerRequest request) {
        log.info("]-----] FollowerHandler::findAllByUserId call [-----[ ");

        ObjectId userId = new ObjectId(request.pathVariable("userId"));

        return ok().build();
    }

    public Mono<ServerResponse> post (ServerRequest request) {
        log.info("]-----] FollowerHandler::post call [-----[ ");

        Follower follower = new Follower();

        ObjectId userId = new ObjectId(request.pathVariable("userId"));

        return request.principal()
                .map(p -> p.getName())
                .flatMap(following -> this.userRepository.findByUsername(following))
                .flatMap(following -> {
                    ArrayList<ObjectId> followerList = new ArrayList<>();

                    followerList.add(following.getId());

                    follower.setFollowerId(followerList);
                    follower.setUserId(userId);

                    return ok().body(this.followerRepository.save(follower),Follower.class);
                })
                .switchIfEmpty(notFound().build());
    }
}