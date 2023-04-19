package lh.wordtree.test;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.*;

public class WinKeyBoard implements Runnable {
    final static User32 lib = User32.INSTANCE;
    private static WinUser.HHOOK hhk;
    private static WinUser.LowLevelKeyboardProc keyboardHook;

    @Override
    public void run() {
        WinDef.HMODULE hMod = Kernel32.INSTANCE.GetModuleHandle(null);
        keyboardHook = (nCode, wParam, info) -> {

            Pointer ptr = info.getPointer();
            long peer = Pointer.nativeValue(ptr);
            Win32VK vk = Win32VK.fromValue(info.vkCode);
            System.out.println(vk.name());
            return lib.CallNextHookEx(hhk, nCode, wParam, new WinDef.LPARAM(peer));
        };
        hhk = lib.SetWindowsHookEx(User32.WH_KEYBOARD_LL, keyboardHook, hMod, 0);
//        lib.UnhookWindowsHookEx(hhk);
    }
}
