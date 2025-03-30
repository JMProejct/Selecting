package selecting.platform.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import selecting.platform.dto.ServicePostResponseDto;
import selecting.platform.repository.ServicePostRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServicePostService {

    private final ServicePostRepository servicePostRepository;

    public Page<ServicePostResponseDto> searchPosts(String keyword, Pageable pageable) {
        return servicePostRepository.searchByKeyword(keyword, pageable);
    }
}
