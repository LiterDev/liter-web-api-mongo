package io.liter.web.api.sample;

import io.liter.web.api.follower.FollowerRepository;
import io.liter.web.api.review.ReviewContentType;
import io.liter.web.api.review.ReviewRepository;
import io.liter.web.api.tag.ReviewTagRepository;
import io.liter.web.api.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.FormFieldPart;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;
import static org.springframework.web.reactive.function.server.ServerResponse.badRequest;
import static org.springframework.web.reactive.function.server.ServerResponse.notFound;

@Slf4j
@Component
public class SampleHandler {

    private final SampleRepository sampleRepository;

    public SampleHandler(SampleRepository sampleRepository) {
        this.sampleRepository = sampleRepository;
    }

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private FollowerRepository followerRepository;
    @Autowired
    private ReviewTagRepository reviewTagRepository;


    /**
     * GET ALL
     */
    public Mono<ServerResponse> getAll(ServerRequest request) {
        log.info("]-----] SampleHandler::getAll call [-----[ ");

        return ServerResponse.ok().build();

        /*
        Flux<Sample> sampleFlux = sampleRepository.findAll();
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(sampleFlux, Sample.class);
        */
    }

    /**
     * GET a Single Object by ID
     */
    public Mono<ServerResponse> getById(ServerRequest request) {
        log.info("]-----] SampleHandler::get call [-----[ ");
        // parse path-variable
        ObjectId id = new ObjectId(request.pathVariable("id"));
        log.debug("]-----] SampleHandler::get id [-----[ {}", id);

        Mono<Sample> sampleMono = sampleRepository.findById(id);
        request.principal()
                .map(p -> {
                    log.debug("]-----] SampleHandler::get id [-----[ {}", p);
                    return p.getName();
                })
                .flatMap(name -> {
                    log.debug("]-----] name [-----[ {}", name);
                    return Mono.just(name);
                })
                .block();

        return sampleMono
                .flatMap(sample -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(fromObject(sample)))
                .switchIfEmpty(notFound().build());
    }

    //@PreAuthorize("hasAuthority('SCOPE_ACCESS')")
    public Mono<ServerResponse> get(ServerRequest request) {
        log.info("]-----] SampleHandler::get call [-----[ ");
        // parse path-variable
        ObjectId id = new ObjectId(request.pathVariable("id"));
        log.debug("]-----] SampleHandler::get id [-----[ {}", id);
        Mono<Sample> sampleMono = sampleRepository.findById(id);

        return request.principal()
                .map(p -> {
                    log.debug("]-----] SampleHandler::get id [-----[ {}", p.getName());
                    return p.getName();
                })
                .flatMap(p ->
                        sampleMono
                                .flatMap(sample -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(fromObject(sample)))
                )
                .switchIfEmpty(notFound().build());
    }

    /**
     * POST a Object
     */
    public Mono<ServerResponse> post(ServerRequest request) {
        log.info("]-----] SampleHandler::post call [-----[ ");
        Mono<Sample> sampleMono = request.bodyToMono(Sample.class);

        return sampleMono
                .flatMap(sample -> sampleRepository.save(sample))
                .flatMap(sample -> ServerResponse.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON_UTF8).body(fromObject(sample)))
                .switchIfEmpty(badRequest().build());

    }

    /**
     * PUT a Object
     */
    public Mono<ServerResponse> put(ServerRequest request) {
        log.info("]-----] SampleHandler::put call [-----[ ");

        ObjectId id = new ObjectId(request.pathVariable("id"));

        Mono<Sample> sampleMono = request.bodyToMono(Sample.class);

        return sampleMono
                .flatMap(sample -> {
                    sample.setId(id);
                    return sampleRepository.save(sample);
                })
                .flatMap(sample -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(fromObject(sample)))
                .switchIfEmpty(badRequest().build());
    }


    /**
     * DELETE a Object
     */
    public Mono<ServerResponse> delete(ServerRequest request) {
        log.info("]-----] SampleHandler::delete call [-----[ ");

        ObjectId id = new ObjectId(request.pathVariable("id"));

        return ServerResponse.ok().build(sampleRepository.deleteById(id));
    }

    /**
     * POST FormData
     */
    public Mono<ServerResponse> postForm(ServerRequest request) {
        log.info("]-----]postForm[-----[");

            /*return request
                    .body(BodyExtractors.toMultipartData())
                    .flatMap(map -> {
                        Map<String, Part> parts = map.toSingleValueMap();
                        try {
                            assertEquals(2, parts.size());
                            assertEquals("foo.txt", ((FilePart) parts.get("fooPart")).filename());
                            assertEquals("bar", ((FormFieldPart) parts.get("barPart")).value());
                        }
                        catch(Exception e) {
                            return Mono.error(e);
                        }
                        return ServerResponse.ok().build();
                    });*/

        Mono<List<FilePart>> imageMono = request.body(BodyExtractors.toParts()).collectList()
                .map(m -> m.stream()
                        .filter(p -> (ReviewContentType.checkCode(p.headers().getContentType()).equals(ReviewContentType.IMAGE.getCode())))
                        .map(p -> ((FilePart) p))
                        .collect(Collectors.toList())
                );

        Mono<List<FormFieldPart>> contentMono = request.body(BodyExtractors.toParts()).collectList()
                .map(m -> m.stream()
                        .filter(p -> (ReviewContentType.checkCode(p.headers().getContentType()).equals(ReviewContentType.CONTENT.getCode())))
                        .map(p -> ((FormFieldPart) p))
                        .collect(Collectors.toList())

                );

        return ServerResponse.ok().build();
    }
}
