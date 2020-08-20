package br.com.caelum.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.telephony.SmsMessage;
import android.widget.Toast;
import br.com.caelum.dao.AlunoDAO;
import br.com.caleum.agenda.R;

public class SmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Chegou SMS!", Toast.LENGTH_LONG).show();

        Object[] pdus = (Object[])intent.getSerializableExtra("pdus");
        byte[] startMsg = (byte[])pdus[0];

        SmsMessage smsMessage;
        String format = intent.getStringExtra("format"); //Exigido a partir do Android 7 - GSM/CDMA
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            smsMessage = SmsMessage.createFromPdu(startMsg,format);
        } else {
            smsMessage = SmsMessage.createFromPdu(startMsg);
        }

        String phone = smsMessage.getDisplayOriginatingAddress();

        AlunoDAO alunoDAO = new AlunoDAO(context);

        if (alunoDAO.existeAluno(phone)) {
            MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.msg);
            mediaPlayer.start();
        }

        alunoDAO.close();
    }
}
