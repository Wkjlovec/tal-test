package cn.diinj.api.model;

/**
 * API response model
 * @param <T> data type
 */
public class Result<T> {
    private Integer code;
    private String message;
    private T data;

    // Default constructor
    public Result() {
    }

    // Constructor with code and message
    public Result(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    // Constructor with code, message, and data
    public Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * Success response with data
     * @param data data
     * @param <T> data type
     * @return Result
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "Success", data);
    }

    /**
     * Success response without data
     * @return Result
     */
    public static <T> Result<T> success() {
        return new Result<>(200, "Success");
    }

    /**
     * Error response with message
     * @param message error message
     * @return Result
     */
    public static <T> Result<T> error(String message) {
        return new Result<>(500, message);
    }

    /**
     * Error response with code and message
     * @param code error code
     * @param message error message
     * @return Result
     */
    public static <T> Result<T> error(Integer code, String message) {
        return new Result<>(code, message);
    }

    // Getters and Setters
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
} 