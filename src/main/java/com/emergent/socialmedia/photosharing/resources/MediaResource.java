package com.emergent.socialmedia.photosharing.resources;

import com.emergent.socialmedia.photosharing.resources.dto.request.CommentsRequestDTO;
import com.emergent.socialmedia.photosharing.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

import static com.emergent.socialmedia.photosharing.resources.MediaResource.REST_USERID_ENDPOINT_PREFIX;

@RestController
@RequestMapping(value = REST_USERID_ENDPOINT_PREFIX)
public class MediaResource {

    @Autowired
    private MediaService mediaService;

    public static final String REST_USERID_ENDPOINT_PREFIX = "/{user_id}";
    public static final String REST_GET_FEED_ENDPOINT = "/feed";
    public static final String REST_MEDIA = "/media/";
    public static final String REST_MEDIA_ACTION = REST_MEDIA+"{media_id}";
    public static final String REST_LIKE_DISLIKE_ENDPOINT = REST_MEDIA_ACTION+"/like"; // PUT, DELETE
    public static final String REST_COMMENT_UNCOMMENT_ENDPOINT = REST_MEDIA_ACTION+"/comment"; // PUT, DELETE
    public static final String REST_GET_LIKED_MEDIA = "/liked/";
    public static final String REST_GET_COMMENTED_MEDIA = "/commented/";


    @PostMapping(path=REST_MEDIA,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> fileUpload(@NotBlank @PathVariable(name="user_id") String userId,
                                             @RequestParam("file") MultipartFile file){
        //TODO validate user when integrated with social media account.
        return ResponseEntity.ok(mediaService.storeMedia(userId, file));
    }

    @GetMapping(path=REST_MEDIA_ACTION)
    public ResponseEntity<Object> fileDownload(@NotBlank @PathVariable(name="media_id") String mediaId){
        Resource resource = mediaService.getMedia(mediaId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @GetMapping(path=REST_GET_FEED_ENDPOINT,
                produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> getPhotoFeedWithAfterAndLimit(@RequestParam(value = "after", required = false) Long after,
                                               @RequestParam(value = "limit", required = false) Integer limit){
        return ResponseEntity.ok(mediaService.getAllMediaOrderByCreatedAtDesc(after, limit));
    }

    @PutMapping(path=REST_LIKE_DISLIKE_ENDPOINT,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> like(@NotBlank @PathVariable(name="user_id") Long userId,
                                       @NotBlank @PathVariable(name="media_id") Long mediaId){
        return ResponseEntity.ok(mediaService.like(userId, mediaId));
    }
    @DeleteMapping(path=REST_LIKE_DISLIKE_ENDPOINT,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> dilike(@NotBlank @PathVariable(name="user_id") Long userId,
                                       @NotBlank @PathVariable(name="media_id") Long mediaId){
        return ResponseEntity.ok(mediaService.dislike(userId, mediaId));
    }

    @PutMapping(path=REST_COMMENT_UNCOMMENT_ENDPOINT,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> comment(@NotBlank @PathVariable(name="user_id") Long userId,
                                       @NotBlank @PathVariable(name="media_id") Long mediaId,
                                          @RequestBody CommentsRequestDTO CommentsRequestDTO){
        return ResponseEntity.ok(mediaService.comment(userId, mediaId, CommentsRequestDTO.getComment()));
    }

    @DeleteMapping(path=REST_COMMENT_UNCOMMENT_ENDPOINT,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> uncomment(@NotBlank @PathVariable(name="user_id") Long userId,
                                         @NotBlank @PathVariable(name="media_id") Long mediaId){
        return ResponseEntity.ok(mediaService.uncomment(userId, mediaId));
    }

    @GetMapping(path=REST_GET_LIKED_MEDIA, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> getAllPhotosLikedByUserId(@NotBlank @PathVariable(name="user_id") Long userId){
        return ResponseEntity.ok(mediaService.getAllMediaLikedByUserId(userId));
    }

    @GetMapping(path=REST_GET_COMMENTED_MEDIA, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> getAllPhotosCommentedByUserId(@NotBlank @PathVariable(name="user_id") Long userId){
        return ResponseEntity.ok(mediaService.getAllMediaCommentedByUserId(userId));
    }
}
