package lets.songko.common.dto;

public record CResponse<T>(T data, boolean success, String message) {
}