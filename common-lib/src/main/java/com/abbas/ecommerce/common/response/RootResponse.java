package com.abbas.ecommerce.common.response;
import com.abbas.ecommerce.common.exception.ApiError;
import com.abbas.ecommerce.common.exception.BaseException;
import com.abbas.ecommerce.common.exception.ErrorMessage;
import com.abbas.ecommerce.common.exception.ErrorMessageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.WebRequest;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RootResponse<T> {

    private boolean result;

    private Integer status;

    private String path;

    private String hostName;

    private LocalDateTime localDateTime;

    private ApiError<T> apiError;

    private T data;


    public static <T> RootResponse<T> ok(T data, WebRequest request) {
        RootResponse<T> rootResponse= new RootResponse<>();
        rootResponse.setResult(true);
        rootResponse.setStatus(200);
        rootResponse.setPath(request.getDescription(false));
        rootResponse.setHostName(getHostName());
        rootResponse.setLocalDateTime(LocalDateTime.now());
        rootResponse.setData(data);
        rootResponse.setApiError(null);
        return rootResponse;

    }

    public static <T> RootResponse<T> error(T error, WebRequest request)  {

        RootResponse<T> rootResponse= new RootResponse<>();
        ApiError<T> apiError= new ApiError<>();
        rootResponse.setResult(false);
        rootResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        rootResponse.setPath(request.getDescription(false));
        rootResponse.setHostName(getHostName());
        rootResponse.setLocalDateTime(LocalDateTime.now());
        rootResponse.setData(null);
        apiError.setMessage(error);
        rootResponse.setApiError(apiError);

        return rootResponse;


    }

    public static String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            throw new BaseException(new ErrorMessage(ErrorMessageType.GENERAL_EXCEPTION,e.getMessage()));
        }

    }

}
