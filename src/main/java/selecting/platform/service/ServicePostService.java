package selecting.platform.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import selecting.platform.dto.ServicePostResponseDto;
import selecting.platform.repository.ServicePostRepository;

import java.math.BigDecimal;


@Service
@RequiredArgsConstructor
public class ServicePostService {

    private final ServicePostRepository servicePostRepository;

    public Page<ServicePostResponseDto> searchPosts(String keyword, BigDecimal minPrice, BigDecimal maxPrice, String location, Pageable pageable) {
        return servicePostRepository.searchWithFilters(keyword, minPrice, maxPrice, location, pageable);
    }
}
