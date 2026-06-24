package com.elgunzamanov.postperformanceanalysis;

import com.elgunzamanov.postperformanceanalysis.config.MetaGraphApiProperties;
import com.elgunzamanov.postperformanceanalysis.dto.MetaGraphResponse;
import com.elgunzamanov.postperformanceanalysis.dto.PostDto;
import com.elgunzamanov.postperformanceanalysis.dto.comment.CommentSummaryDto;
import com.elgunzamanov.postperformanceanalysis.dto.comment.CommentsDto;
import com.elgunzamanov.postperformanceanalysis.dto.reaction.ReactionSummaryDto;
import com.elgunzamanov.postperformanceanalysis.dto.reaction.ReactionsDto;
import com.elgunzamanov.postperformanceanalysis.exception.MetaGraphApiException;
import com.elgunzamanov.postperformanceanalysis.service.MetaGraphApiService;
import lombok.NonNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MetaGraphApiServiceTest {
	@Mock private RestClient restClient;
	@Mock private MetaGraphApiProperties metaGraphApiProperties;
	@Mock private ObjectMapper objectMapper;
	@Mock private RestClient.RequestHeadersUriSpec<?> requestHeadersUriSpec;
	@Mock private RestClient.RequestHeadersSpec<?> requestHeadersSpec;
	@Mock private RestClient.ResponseSpec responseSpec;
	
	@InjectMocks
	private MetaGraphApiService metaGraphApiService;
	
	private @NonNull PostDto buildSamplePost() {
		ReactionsDto reactions = new ReactionsDto(List.of(), new ReactionSummaryDto(5, "NONE"));
		CommentsDto comments = new CommentsDto(List.of(), new CommentSummaryDto("ranked", 3, true));
		return new PostDto("1", "Test message", OffsetDateTime.now(), reactions, comments);
	}
	
	@SuppressWarnings({"unchecked"})
	private void mockRestClientChain(String responseBody) {
		lenient().when(metaGraphApiProperties.pageId()).thenReturn("123456789");
		lenient().when(metaGraphApiProperties.pageAccessToken()).thenReturn("test_token");
		lenient().when(metaGraphApiProperties.fields()).thenReturn("id,message,created_time,reactions,comments");
		lenient().when(metaGraphApiProperties.limit()).thenReturn(10);
		
		doReturn(requestHeadersUriSpec).when(restClient).get();
		doReturn(requestHeadersSpec).when(requestHeadersUriSpec).uri(any(Function.class));
		doReturn(responseSpec).when(requestHeadersSpec).retrieve();
		lenient().doReturn(responseBody).when(responseSpec).body(String.class);
	}
	
	@Test
	@SuppressWarnings({"unchecked"})
	void getPosts_shouldReturnPosts_whenValidResponse() {
		String json = "{\"data\":[...]}";
		MetaGraphResponse<PostDto> response = new MetaGraphResponse<>(List.of(buildSamplePost()));
		
		mockRestClientChain(json);
		when(objectMapper.readValue(eq(json), any(TypeReference.class))).thenReturn(response);
		
		List<PostDto> result = metaGraphApiService.getPosts();
		
		assertThat(result).hasSize(1);
		assertThat(result.getFirst().id()).isEqualTo("1");
		assertThat(result.getFirst().message()).isEqualTo("Test message");
	}
	
	@Test
	@SuppressWarnings({"unchecked"})
	void getPosts_shouldReturnEmptyList_whenResponseIsNull() {
		String json = "null";
		
		mockRestClientChain(json);
		when(objectMapper.readValue(eq(json), any(TypeReference.class))).thenReturn(null);
		
		List<PostDto> result = metaGraphApiService.getPosts();
		
		assertThat(result).isEmpty();
	}
	
	@Test
	@SuppressWarnings({"unchecked"})
	void getPosts_shouldThrowMetaGraphApiException_whenParsingFails() {
		String invalidJson = "invalid_json";
		
		mockRestClientChain(invalidJson);
		when(objectMapper.readValue(eq(invalidJson), any(TypeReference.class)))
			.thenThrow(new RuntimeException("Parse error"));
		
		assertThatThrownBy(() -> metaGraphApiService.getPosts())
			.isInstanceOf(MetaGraphApiException.class)
			.hasMessageContaining("Meta Graph API response could not be parsed.");
	}
	
	@Test
	void getPosts_shouldThrowException_whenRestClientFails() {
		mockRestClientChain("ignored");
		doThrow(new RuntimeException("Connection refused")).when(responseSpec).body(String.class);
		
		assertThatThrownBy(() -> metaGraphApiService.getPosts())
			.isInstanceOf(RuntimeException.class)
			.hasMessageContaining("Connection refused");
	}
	
	@Test
	void getPageSize_shouldReturnValueFromProperties() {
		when(metaGraphApiProperties.pageSize()).thenReturn(5);
		
		assertThat(metaGraphApiService.getPageSize()).isEqualTo(5);
	}
}
