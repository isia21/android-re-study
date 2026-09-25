package sg.vantagepoint.uncrackable2;

/* JADX INFO: loaded from: classes.dex */
public class CodeCheck {
    private native boolean bar(byte[] bArr);

    /* JADX INFO: renamed from: a */
    public boolean IsAnswerCorrect(String str) {
        return bar(str.getBytes());
    }
}
