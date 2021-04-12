package com.example.btl_ltuddd_electricstore.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.btl_ltuddd_electricstore.Activity.xac_nhan_don_hang_Activity;
import com.example.btl_ltuddd_electricstore.R;
import com.example.btl_ltuddd_electricstore.object.PhuongThucDonHang;
import com.example.btl_ltuddd_electricstore.object.TenUser;
import com.example.btl_ltuddd_electricstore.object.ThongBao;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class service_notification extends Service {
    DatabaseReference mData;
    NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "1");

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // mo activity xac nhan don hang khi click thong bao
        Intent intent21 = new Intent(this, xac_nhan_don_hang_Activity.class);
        intent21.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent21, PendingIntent.FLAG_UPDATE_CURRENT);

        mData = FirebaseDatabase.getInstance().getReference();
        // hien thi thong bao cho nhan vien
        mData.child("ThongBao").child("ChoXacNhan").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ThongBao thongBao= snapshot.getValue(ThongBao.class);
                // tao thong bao
                    if (TenUser.quyen.equals("Nhân viên")) {
                        Log.i("ten",TenUser.quyen);
                        builder.setContentTitle("Thông báo");
                        builder.setContentText(thongBao.getThongTin());
                        builder.setSmallIcon(R.drawable.ic_andoid);
                        builder.setAutoCancel(true);
                        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
                        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(service_notification.this);
                        managerCompat.notify(1, builder.build());
                    }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return START_STICKY;
    }

}
