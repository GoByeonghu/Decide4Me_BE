package com.sfz.rest_api.controller;

import com.sfz.rest_api.dto.LongIDResponse;
import com.sfz.rest_api.dto.ResponseForm;
import com.sfz.rest_api.dto.VoteOptionResponse;
import com.sfz.rest_api.model.Post;
import com.sfz.rest_api.model.User;
import com.sfz.rest_api.model.VoteOption;
import com.sfz.rest_api.model.VoteParticipant;
import com.sfz.rest_api.service.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vote")
public class VoteController {
    private final UserService userService;
    private final PostService postService;
    private final VoteOptionService voteOptionService;
    private final VoteParticipantService voteParticipantService;

    @Autowired
    public VoteController(UserService userService,
                          PostService postService,
                          VoteOptionService voteOptionService,
                          VoteParticipantService voteParticipantService) {
        this.userService = userService;
        this.postService = postService;
        this.voteOptionService = voteOptionService;
        this.voteParticipantService = voteParticipantService;
    }

    @Transactional
    @PostMapping("/{id}/agree")
    public ResponseEntity<ResponseForm> voteAgree(@PathVariable("id") Long vote_id,
                                                  @RequestParam(name = "user_id", required = false, defaultValue = "created_at") String nickname) {
        // 이전 투표 참여여부 확인
        User user = userService.getUserByNickname(nickname);
        VoteParticipant voteParticipant = voteParticipantService.getVoteParticipantByUserIdAndVoteOptionId(user.getId(), vote_id);
        if (voteParticipant != null) {
            return new ResponseEntity<>(new ResponseForm("already participant", null), HttpStatus.CONFLICT);
        }
        VoteOption voteOption = voteOptionService.getVoteOptionById(vote_id);
        // 투표참여자 추가
        VoteParticipant newVoteParticipant = new VoteParticipant();
        newVoteParticipant.setVoteOption(voteOption);
        newVoteParticipant.setPost(voteOption.getPost());
        newVoteParticipant.setUser(user);
        voteParticipantService.createVoteParticipant(newVoteParticipant);

        // 보우트 옵션 카운트 증가
        voteOption.setCount(voteOption.getCount() +1 );
        voteOption = voteOptionService.updateVoteOption(voteOption);
        VoteOptionResponse voteOptionResponse = new VoteOptionResponse(voteOption);
        return new ResponseEntity<>(new ResponseForm("ok", voteOptionResponse), HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/{id}/disagree")
    public ResponseEntity<ResponseForm> voteDisagree(@PathVariable("id") Long vote_id,
                                                     @RequestParam(name = "user_id", required = false, defaultValue = "created_at") String nickname) {
        //이전 투표 참여여부 확인
        User user = userService.getUserByNickname(nickname);
        VoteParticipant voteParticipant = voteParticipantService.getVoteParticipantByUserIdAndVoteOptionId(user.getId(), vote_id);
        if (voteParticipant == null) {
            return new ResponseEntity<>(new ResponseForm("error", null), HttpStatus.NOT_FOUND);
        }
        //투표 참여 삭제
        voteParticipantService.deleteVoteParticipant(voteParticipant.getId());
        //보웉 옵션 카운트 1감소
        VoteOption voteOption = voteOptionService.getVoteOptionById(vote_id);
        voteOption.setCount(voteOption.getCount() -1 );
        voteOption = voteOptionService.updateVoteOption(voteOption);
        VoteOptionResponse voteOptionResponse = new VoteOptionResponse(voteOption);
        return new ResponseEntity<>(new ResponseForm("ok", voteOptionResponse), HttpStatus.OK);
    }

    @PostMapping("/is-participant")
    public ResponseEntity<ResponseForm> getVoteOptionByUserIdAndPostId(@RequestParam(name = "post_id", required = false) Long post_id,
                                                     @RequestParam(name = "user_id", required = false) String nickname) {

        User user = userService.getUserByNickname(nickname);
        Post post = postService.getPostById(post_id);
        VoteParticipant voteParticipant = voteParticipantService.getVoteParticipantByUserIdAndPostId(user.getId(),post.getId() );
        if (voteParticipant == null) {
            return new ResponseEntity<>(new ResponseForm("no, don't participate", null), HttpStatus.OK);
        }
        LongIDResponse longIDResponse = new LongIDResponse(voteParticipant.getVoteOption().getId());
        return new ResponseEntity<>(new ResponseForm("ok", longIDResponse), HttpStatus.OK);
    }

}
