package iconExtract;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.WString;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.Shell32;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.PointerByReference;

public interface Shell32Extra extends Shell32 {
    Shell32Extra INSTANCE = (Shell32Extra) Native.load("shell32", Shell32Extra.class);

    WinNT.HRESULT SHCreateItemFromParsingName(WString var1, Pointer var2, Guid.REFIID var3, PointerByReference var4);
}
