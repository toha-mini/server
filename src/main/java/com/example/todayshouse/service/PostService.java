package com.example.todayshouse.service;

import com.example.todayshouse.domain.dto.request.PostRequestDto;
import com.example.todayshouse.domain.dto.response.DetailPostResponseDto;
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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j(topic = "PostService")
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final QueryRepository queryRepository;
    private final ScrapRepository scrapRepository;
    private final LikesRepository likesRepository;
    private final AwsS3Util awsS3Util;

    public ResponseEntity<MessageResponseDto> createPost(Member member, @Valid PostRequestDto requestDto, MultipartFile titleImgMultiPartFile, MultipartFile subImg1MultiPartFile, MultipartFile subImg2MultiPartFile) {
        //urls
        String titleImgUrl;
        String subImgurl1 = "";
        String subImgurl2 = "";

        // s3에 이미지 파일 저장 및 url
        titleImgUrl = awsS3Util.uploadImgFile(titleImgMultiPartFile, "postImg");
        if(subImg1MultiPartFile != null) {
            subImgurl1 = awsS3Util.uploadImgFile(subImg1MultiPartFile, "postImg");
        }
        if(subImg2MultiPartFile != null) {
            subImgurl2 = awsS3Util.uploadImgFile(subImg2MultiPartFile, "postImg");
        }

        // Entity 객체 생성
        Post post = new Post(member, requestDto.getContent(), titleImgUrl, subImgurl1, subImgurl2);

        // DB 저장
        postRepository.save(post);

        MessageResponseDto response = new MessageResponseDto("업로드 완료", 201, "CREATED");
        return ResponseEntity.status(201).body(response);
    }

    public List<PostResponseDto> getPostList(Optional<UserDetailsImpl> userDetails) {
        List<Post> postList = postRepository.findAll();
        List<PostResponseDto> postResponseDtoList = new ArrayList<>();

        boolean isScrap = false;

        for (Post post : postList) {
            if (userDetails.isPresent()) {
                isScrap = checkPostScrap(post, userDetails.get().getMember());
            }
            postResponseDtoList.add(new PostResponseDto(post, isScrap));
        }

        return postResponseDtoList;
    }

    public DetailPostResponseDto getPost(Long postId, Optional<UserDetailsImpl> userDetails) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("게시글이 존재 하지 않습니다."));

        Long likeCount = queryRepository.findLikeCount(postId);
        Long scrapCount = queryRepository.findScrapCount(postId);

        boolean isScrap = false;
        boolean isLike = false;

        if (userDetails.isPresent()) {
            isScrap = checkPostScrap(post, userDetails.get().getMember());
            isLike = checkPostLike(post, userDetails.get().getMember());
        }

        return new DetailPostResponseDto(post, likeCount, scrapCount, isLike, isScrap);
    }

    public boolean checkPostLike(Post post, Member member) {
        return likesRepository.existsByPostIdAndMemberId(post.getId(), member.getId());
    }

    public boolean checkPostScrap(Post post, Member member) {
        return scrapRepository.existsByPostIdAndMemberId(post.getId(), member.getId());
    }
}
