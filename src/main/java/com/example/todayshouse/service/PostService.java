package com.example.todayshouse.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.example.todayshouse.domain.StatusEnum;
import com.example.todayshouse.domain.dto.request.PostRequestDto;
import com.example.todayshouse.domain.dto.response.DetailPostResponseDto;
import com.example.todayshouse.domain.dto.response.LikePostResponseDto;
import com.example.todayshouse.domain.dto.response.MessageResponseDto;
import com.example.todayshouse.domain.dto.response.PostResponseDto;
import com.example.todayshouse.domain.entity.Member;
import com.example.todayshouse.domain.entity.Post;
import com.example.todayshouse.repository.LikesRepository;
import com.example.todayshouse.repository.PostRepository;
import com.example.todayshouse.repository.QueryRepository;
import com.example.todayshouse.repository.ScrapRepository;
import com.example.todayshouse.security.userdetails.UserDetailsImpl;
import com.example.todayshouse.util.AwsS3Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.todayshouse.domain.StatusEnum.*;

@Slf4j(topic = "PostService")
@Service
@RequiredArgsConstructor
public class PostService {

    private final String DIR_NAME = "postImg";

    private final PostRepository postRepository;
    private final QueryRepository queryRepository;
    private final ScrapRepository scrapRepository;
    private final LikesRepository likesRepository;
    private final AwsS3Util awsS3Util;

    public ResponseEntity<MessageResponseDto> createPost(Member member, PostRequestDto requestDto, MultipartFile titleImgMultiPartFile, MultipartFile subImg1MultiPartFile, MultipartFile subImg2MultiPartFile) {
        //urls
        String titleImgUrl;
        String subImgurl1 = "";
        String subImgurl2 = "";

        // s3에 이미지 파일 저장 및 url
        titleImgUrl = awsS3Util.uploadImgFile(titleImgMultiPartFile, DIR_NAME);
        if (subImg1MultiPartFile != null) {
            subImgurl1 = awsS3Util.uploadImgFile(subImg1MultiPartFile, DIR_NAME);
        }
        if (subImg2MultiPartFile != null) {
            subImgurl2 = awsS3Util.uploadImgFile(subImg2MultiPartFile, DIR_NAME);
        }

        // Entity 객체 생성
        Post post = new Post(member, requestDto.getContent(), titleImgUrl, subImgurl1, subImgurl2);

        // DB 저장
        postRepository.save(post);

        MessageResponseDto response = new MessageResponseDto("업로드 완료", CREATED.getCode(), CREATED.getMessage());
        return ResponseEntity.status(CREATED.getCode()).body(response);
    }

    public ResponseEntity<List<PostResponseDto>> getPostList(Optional<UserDetailsImpl> userDetails) {
        List<Post> postList = postRepository.findAll();

        List<PostResponseDto> postResponseDtoList = postList.stream()
                .map(post -> new PostResponseDto(post, isScrap(userDetails, post)))
                .toList();

        return ResponseEntity.status(OK.getCode()).body(postResponseDtoList);
    }

    private boolean isScrap(Optional<UserDetailsImpl> userDetails, Post post) {
        return userDetails.isPresent() && checkPostScrap(post, userDetails.get().getMember());
    }

    public ResponseEntity<DetailPostResponseDto> getPost(Long postId, Optional<UserDetailsImpl> userDetails) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("없는 포스트입니다."));

        Long likeCount = queryRepository.findLikeCount(postId);
        Long scrapCount = queryRepository.findScrapCount(postId);
        boolean isScrap = userDetails.isPresent() && checkPostScrap(post, userDetails.get().getMember());
        boolean isLike = userDetails.isPresent() && checkPostLike(post, userDetails.get().getMember());

        DetailPostResponseDto detailPostResponseDto = new DetailPostResponseDto(post, likeCount, scrapCount, isLike, isScrap);

        return ResponseEntity.status(OK.getCode()).body(detailPostResponseDto);
    }

    private boolean checkPostLike(Post post, Member member) {
            return likesRepository.existsByPostIdAndMemberId(post.getId(), member.getId());
    }

    private boolean checkPostScrap(Post post, Member member) {
            return scrapRepository.existsByPostIdAndMemberId(post.getId(), member.getId());
    }

    public ResponseEntity<List<LikePostResponseDto>> getPostsByLike() {
        List<LikePostResponseDto> likePostResponseDtoList = queryRepository.findPostsSortedByLikeCount().stream()
                .map(result -> {
                    Long postId = result.getId();
                    Long likeCount = queryRepository.findLikeCount(postId);
                    return new LikePostResponseDto(result, likeCount);
                })
                .toList();

        return ResponseEntity.status(OK.getCode()).body(likePostResponseDtoList);
    }
}
