package com.example.smartlocker10;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build.VERSION;
import androidx.core.app.NotificationCompat.Builder;

import com.example.smartlocker10.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.messaging.RemoteMessage.Notification;
import java.util.Map;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(
        mv = {1, 1, 18},
        bv = {1, 0, 3},
        k = 1,
        d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\b\u0010\u0005\u001a\u00020\u0006H\u0016J\u0010\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\tH\u0016J\u0010\u0010\n\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\u0004H\u0016J\u0010\u0010\f\u001a\u00020\u00062\u0006\u0010\r\u001a\u00020\u0004H\u0016J\u0018\u0010\u000e\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\u00042\u0006\u0010\u000f\u001a\u00020\u0010H\u0016J\u001a\u0010\u0011\u001a\u00020\u00062\b\u0010\u0012\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0013\u001a\u00020\u0004H\u0002J\u0010\u0010\u0014\u001a\u00020\u00062\u0006\u0010\r\u001a\u00020\u0004H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082D¢\u0006\u0002\n\u0000¨\u0006\u0015"},
        d2 = {"Lorg/techtown/firebase_test/MyFirebaseMessagingService;", "Lcom/google/firebase/messaging/FirebaseMessagingService;", "()V", "TAG", "", "onDeletedMessages", "", "onMessageReceived", "remoteMessage", "Lcom/google/firebase/messaging/RemoteMessage;", "onMessageSent", "p0", "onNewToken", "token", "onSendError", "p1", "Ljava/lang/Exception;", "sendNotification", "title", "body", "sendRegistrationToServer", "app_debug"}
)
public final class MyFirebaseMessagingService extends FirebaseMessagingService {
    private final String TAG = "FirebaseTest";


    //메시지가 수신되면 호출
    public void onMessageReceived(@NotNull RemoteMessage remoteMessage) {
        Intrinsics.checkParameterIsNotNull(remoteMessage, "remoteMessage");
        Map var10000 = remoteMessage.getData();
        Intrinsics.checkExpressionValueIsNotNull(var10000, "remoteMessage.data");
        Map var2 = var10000;
        if (!var2.isEmpty()) {
            Notification var10001 = remoteMessage.getNotification();
            String var3 = var10001 != null ? var10001.getTitle() : null;
            Notification var10002 = remoteMessage.getNotification();
            String var4 = var10002 != null ? var10002.getBody() : null;
            if (var4 == null) {
                Intrinsics.throwNpe();
            }

            Intrinsics.checkExpressionValueIsNotNull(var4, "remoteMessage.notification?.body!!");
            this.sendNotification(var3, var4);
        }

    }
    // 메세지를 삭제 시 호출
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    public void onMessageSent(@NotNull String p0) {
        Intrinsics.checkParameterIsNotNull(p0, "p0");
        super.onMessageSent(p0);
    }
    //성공 했을때 호출
    public void onSendError(@NotNull String p0, @NotNull Exception p1) {
        Intrinsics.checkParameterIsNotNull(p0, "p0");
        Intrinsics.checkParameterIsNotNull(p1, "p1");
        super.onSendError(p0, p1);
    }

    //토큰이 생성 될 때 호출
    public void onNewToken(@NotNull String token) {
        Intrinsics.checkParameterIsNotNull(token, "token");
        super.onNewToken(token);
        this.sendRegistrationToServer(token);
    }

    private void sendNotification(String title, String body) {
        Intent intent = new Intent((Context)this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 액티비티 중복 생성 방지
        PendingIntent pendingIntent = PendingIntent.getActivity((Context)this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String channelId = "channel"; // 채널 아이디
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(2);
        Builder notificationBuilder = (new Builder((Context)this, channelId)).setContentTitle((CharSequence)title).setContentText((CharSequence)body).setAutoCancel(true).setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        Object var10000 = this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (var10000 == null) {
            throw new TypeCastException("null cannot be cast to non-null type android.app.NotificationManager");
        } else {
            NotificationManager notificationManager = (NotificationManager)var10000;
            if (VERSION.SDK_INT >= 26) {
                NotificationChannel channel = new NotificationChannel(channelId, (CharSequence)"Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }

            notificationManager.notify(0, notificationBuilder.build());
        }
    }
    // 받은 토큰을 서버로 전송
    private final void sendRegistrationToServer(String token) {
    }
}

/*

FirebaseMessagingService를 푸쉬알람 받는 것

onMessageReceived : 메세지가 수신되면 호출
onDeletedMessages : Firebase Cloud Messaging Server 가 대기중인 메세지를 삭제 시 호출
onMessageSent : 메세지가 서버로 전송 성공 했을때 호출
onSendError : 메세지가 서버로 전송 실패 했을때 호출
onNewToken : 새로운 토큰이 생성 될 때 호출
 */
