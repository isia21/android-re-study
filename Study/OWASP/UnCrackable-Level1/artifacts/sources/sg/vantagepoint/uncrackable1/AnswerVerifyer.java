package sg.vantagepoint.uncrackable1;

import android.util.Base64;
import android.util.Log;

/* JADX INFO: renamed from: sg.vantagepoint.uncrackable1.a */
/* JADX INFO: loaded from: classes.dex */
public class AnswerVerifyer {
    /* JADX INFO: renamed from: a */
    public static boolean IsStringCorrect(String struserInput) {
        byte[] strCorrectAnswer;
        byte[] bArr = new byte[0];
        try {
            strCorrectAnswer = sg.vantagepoint.a.a.a(ParseHEX2ByteArray("8d127684cbc37c17616d806cf50473cc"), Base64.decode("5UJiFctbmgbDoLXmpL12mkno8HT4Lv8dlat8FxR2GOc=", 0));
        } catch (Exception e) {
            Log.d("CodeCheck", "AES error:" + e.getMessage());
            strCorrectAnswer = bArr;
        }
        return struserInput.equals(new String(strCorrectAnswer));
    }

    /* JADX INFO: renamed from: b */
    public static byte[] ParseHEX2ByteArray(String str) {
        int length = str.length();
        byte[] bArr = new byte[length / 2];
        for (int i = 0; i < length; i += 2) {
            bArr[i / 2] = (byte) ((Character.digit(str.charAt(i), 16) << 4) + Character.digit(str.charAt(i + 1), 16));
        }
        return bArr;
    }
}
