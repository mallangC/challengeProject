package com.zerobase.challengeproject.member.entity;

import com.zerobase.challengeproject.account.entity.AccountDetail;
import com.zerobase.challengeproject.account.entity.Refund;
import com.zerobase.challengeproject.exception.CustomException;
import com.zerobase.challengeproject.exception.ErrorCode;
import com.zerobase.challengeproject.member.domain.form.MemberSignupForm;
import com.zerobase.challengeproject.type.MemberType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    @Column(length = 50, nullable = false)
    private String memberId;
    @Column(length = 50, nullable = false)
    private String memberName;
    @Column(length = 150, nullable = false)
    private String password;
    @Column(length = 50, nullable = false)
    private String nickname;
    @Column(length = 20, nullable = false)
    private String phoneNum;
    @Column(length = 50, nullable = false)
    private String email;
    private LocalDateTime registerDate;

    private boolean emailAuthYn;
    private LocalDateTime emailAuthDate;
    private String emailAuthKey;

    @Column(nullable = false)
    private MemberType memberType;

    private Long account;
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<AccountDetail> accountDetails;

    /**
     * 이메일 인증을 완료하는 메서드. 이미 인증된 경우 처리 생략
     */
    public void completeEmailAuth() {
        if (!this.emailAuthYn) {
            this.emailAuthYn = true;
            this.emailAuthDate = LocalDateTime.now();
        }
    }

    public static Member from(MemberSignupForm form, String password, String emailAuthKey) {
        return Member.builder()
                .memberId(form.getMemberId())
                .memberName(form.getMemberName())
                .password(password)
                .nickname(form.getNickname())
                .phoneNum(form.getPhoneNum())
                .emailAuthKey(emailAuthKey)
                .emailAuthYn(false)
                .memberType(MemberType.USER)
                .registerDate(LocalDateTime.now())
                .email(form.getEmail())
                .build();
    }

    public void chargeAccount(Long amount) {
        this.account += amount;
    }

    public void refundAccount(AccountDetail detail, Refund refund) {
        if (this.account < detail.getAmount()) {
            throw new CustomException(ErrorCode.NOT_ENOUGH_MONEY_TO_REFUND);
        } else if (!detail.isCharge()) {
            throw new CustomException(ErrorCode.NOT_CHARGE_DETAIL);
        } else if (detail.isRefunded()) {
            throw new CustomException(ErrorCode.ALREADY_REFUNDED);
        }
        detail.refundTrue();
        refund.refundTrue();
        this.account -= detail.getAmount();
    }

}
