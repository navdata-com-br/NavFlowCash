package br.com.navdata.flowcash.dto.response;

import br.com.navdata.flowcash.entity.UserEntity;
import java.time.LocalDateTime;

public record UserProfileResponse(
    String id,
    String email,
    String displayName,
    String photoUrl,
    String role,
    LocalDateTime accessUntil,
    boolean isExpired,
    boolean isTrial,
    LocalDateTime createdAt,
    String partnerEmail,
    String partnerName,
    String partnerUid,
    String invitedBy,
    String primaryOwnerEmail,
    String primaryOwnerName
) {
    public static UserProfileResponse fromEntity(UserEntity user) {
        boolean expired = user.isAccessExpired();
        boolean trial = user.getCreatedAt() != null && user.getAccessUntil() != null &&
                user.getAccessUntil().isBefore(user.getCreatedAt().plusDays(31));
        return new UserProfileResponse(
            user.getId(),
            user.getEmail(),
            user.getDisplayName(),
            user.getPhotoUrl(),
            user.getRole(),
            user.getAccessUntil(),
            expired,
            trial,
            user.getCreatedAt(),
            user.getPartnerEmail(),
            user.getPartnerName(),
            user.getPartnerUid(),
            user.getInvitedBy(),
            user.getPrimaryOwnerEmail(),
            user.getPrimaryOwnerName()
        );
    }
}
