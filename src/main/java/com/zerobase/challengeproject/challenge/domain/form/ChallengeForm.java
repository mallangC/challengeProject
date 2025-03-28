    package com.zerobase.challengeproject.challenge.domain.form;

    import jakarta.validation.constraints.NotBlank;
    import jakarta.validation.constraints.Size;
    import lombok.Builder;
    import lombok.Getter;
    import lombok.Setter;

    import java.time.LocalDateTime;

    @Getter
    @Setter
    @Builder
    public class ChallengeForm {

        @NotBlank(message = "제목을 작성해 주세요.")
        @Size(min = 3, max = 100, message = "제목은 3자 이상, 100자 이하로 입력해야 합니다.")
        private String title;

        @NotBlank(message = "챌린지 설명을 작성해 주세요.")
        @Size(min = 10, max = 500, message = "설명은 10자 이상, 500자 이하로 입력해야 합니다.")
        private String description;

        @NotBlank
        private String standard;

        private String img;

        private Integer participant;

        private Integer min_deposit;

        private Integer max_deposit;

        private LocalDateTime startDate;

        private LocalDateTime endDate;
    }
