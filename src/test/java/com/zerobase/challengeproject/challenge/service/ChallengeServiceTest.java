package com.zerobase.challengeproject.challenge.service;


import com.zerobase.challengeproject.challenge.ChallengeService;
import com.zerobase.challengeproject.challenge.domain.dto.BaseResponseDto;
import com.zerobase.challengeproject.challenge.domain.form.ChallengeForm;
import com.zerobase.challengeproject.challenge.entity.Challenge;
import com.zerobase.challengeproject.challenge.repository.ChallengeRepository;
import com.zerobase.challengeproject.exception.CustomException;
import com.zerobase.challengeproject.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
public class ChallengeServiceTest {

    @Mock
    private ChallengeRepository challengeRepository;

    @InjectMocks
    private ChallengeService challengeService;

    private ChallengeForm challengeForm;
    private Long challengeId;

    // Challenge 객체 생성 메서드
    private Challenge createChallenge(Long id, String title) {
        return Challenge.builder()
                .id(id)
                .title(title)
                .img(null)
                .participant(10)
                .description("설명")
                .min_deposit(1000)
                .max_deposit(5000)
                .standard("기준")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(7))
                .createAt(LocalDateTime.now())
                .build();
    }

    // ChallengeForm 생성 메서드
    private ChallengeForm createChallengeForm() {
        return ChallengeForm.builder()
                .title("챌린지 제목")
                .description("설명")
                .standard("기준")
                .participant(5)
                .min_deposit(1000)
                .max_deposit(5000)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(7))
                .build();
    }

    @BeforeEach
    void setUp() {
        challengeId = 1L;
        challengeForm = createChallengeForm();
    }

    @Test
    void getAllChallenges_success() {
        // Given
        List<Challenge> challengeList = List.of(
                createChallenge(1L, "챌린지 1"),
                createChallenge(2L, "챌린지 2"),
                createChallenge(3L, "챌린지 3")
        );
        Pageable pageable = PageRequest.of(0, 10);
        Page<Challenge> challengePage = new PageImpl<>(challengeList, pageable, challengeList.size());

        given(challengeRepository.findAll(pageable)).willReturn(challengePage);

        // When
        ResponseEntity<BaseResponseDto<Page<Challenge>>> response = challengeService.getAllChallenges(pageable);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData().getContent()).hasSize(3);
        assertThat(response.getBody().getData().getContent().get(0).getTitle()).isEqualTo("챌린지 1");
    }

    @Test
    void getAllChallenges_failure() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Challenge> emptyPage = Page.empty();

        given(challengeRepository.findAll(pageable)).willReturn(emptyPage);

        // When & Then
        assertThatThrownBy(() -> challengeService.getAllChallenges(pageable))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.NOT_FOUND_CHALLENGES.getMessage());
    }

    @Test
    void getChallengeDetail_success() {

        // Given
        Challenge challenge = createChallenge(challengeId, "챌린지 제목");
        given(challengeRepository.findById(challengeId)).willReturn(Optional.of(challenge));

        // When
        ResponseEntity<BaseResponseDto<Challenge>> response = challengeService.getChallengeDetail(challengeId);

        // Then
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody().getData().getTitle()).isEqualTo("챌린지 제목");
        assertThat(response.getBody().getData().getId()).isEqualTo(1L);
    }
    @Test
    void getChallengeDetail_failure() {
        // Given
        given(challengeRepository.findById(challengeId)).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> challengeService.getChallengeDetail(challengeId))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.NOT_FOUND_CHALLENGE.getMessage());
    }

    @Test
    void updateChallenge_success() {
        // Given
        Challenge existingChallenge = createChallenge(challengeId, "기존 제목");
        given(challengeRepository.findById(challengeId)).willReturn(Optional.of(existingChallenge));

        // When
        ResponseEntity<BaseResponseDto<Challenge>> response = challengeService.updateChallenge(challengeId, challengeForm);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getData().getTitle()).isEqualTo(challengeForm.getTitle());
        assertThat(response.getBody().getData().getDescription()).isEqualTo(challengeForm.getDescription());
    }

    @Test
    void updateChallenge_invalidDepositAmount() {

        // Given
        challengeForm.setMin_deposit(6000);
        challengeForm.setMax_deposit(5000);

        // When & Then
        assertThatThrownBy(() -> challengeService.updateChallenge(challengeId, challengeForm))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_DEPOSIT_AMOUNT.getMessage());
    }

    @Test
    void updateChallenge_invalidParticipantNumber() {

        // Given
        challengeForm.setParticipant(0);

        // When & Then
        assertThatThrownBy(() -> challengeService.updateChallenge(challengeId, challengeForm))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_PARTICIPANT_NUMBER.getMessage());
    }

    @Test
    void updateChallenge_invalidDateRange() {
        // Given
        challengeForm.setStartDate(LocalDateTime.now().plusDays(10));

        // When & Then
        assertThatThrownBy(() -> challengeService.updateChallenge(challengeId, challengeForm))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_DATE_RANGE.getMessage());
    }

    @Test
    void updateChallenge_challengeNotFound() {
        // Given
        given(challengeRepository.findById(challengeId)).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> challengeService.updateChallenge(challengeId, challengeForm))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.NOT_FOUND_CHALLENGE.getMessage());
    }

    @Test
    void deleteChallenge_success() {
        // Given
        given(challengeRepository.findById(challengeId)).willReturn(Optional.of(createChallenge(challengeId,"삭제용 챌린지")));

        // When
        ResponseEntity<BaseResponseDto<Challenge>> response = challengeService.deleteChallenge(challengeId);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getMessage()).isEqualTo("챌린지 삭제 성공");
    }


    @Test
    void deleteChallenge_notFound() {
        // Given
        given(challengeRepository.findById(challengeId)).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> challengeService.deleteChallenge(challengeId))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.NOT_FOUND_CHALLENGE.getMessage());
    }
}
