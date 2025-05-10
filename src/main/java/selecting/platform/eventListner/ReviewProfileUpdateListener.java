package selecting.platform.eventListner;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import selecting.platform.event.UserProfileUpdateEvent;
import selecting.platform.service.ReviewService;

@Component
@RequiredArgsConstructor
public class ReviewProfileUpdateListener {

    private final ReviewService reviewService;

    @Async
    @EventListener
    public void handleProfileImageUpdate(UserProfileUpdateEvent event) {
        reviewService.updateProfileImageReviews(event.getUserId(), event.getProfileImage());
    }
}
