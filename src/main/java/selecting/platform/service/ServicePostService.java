package selecting.platform.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import selecting.platform.dto.ServicePostResponseDto;
import selecting.platform.repository.ServicePostRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServicePostService {

    private final ServicePostRepository servicePostRepository;

    public List<ServicePostResponseDto> searchPosts(String keyword) {
        return servicePostRepository.searchByKeyword(keyword);
    }
}
