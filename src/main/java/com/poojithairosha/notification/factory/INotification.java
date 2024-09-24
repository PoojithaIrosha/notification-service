package com.poojithairosha.notification.factory;

import com.poojithairosha.notification.dto.NotificationDto;

import java.util.concurrent.CompletableFuture;

public interface INotification {

    CompletableFuture<Void> sendNotification(NotificationDto notificationDto);

}
