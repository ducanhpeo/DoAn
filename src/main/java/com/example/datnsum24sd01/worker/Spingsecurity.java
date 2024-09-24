package com.example.datnsum24sd01.worker;
import com.example.datnsum24sd01.security.admin.CustomNhanVienDetail;

import com.example.datnsum24sd01.security.shop.CustomKhachHangDetail;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class Spingsecurity {
    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // ckeck nhân viên đăng nhập
        if (authentication != null && authentication.isAuthenticated()) {
            // Lấy thông tin chi tiết nhân viên
            Object principal = authentication.getPrincipal();


            if (principal instanceof CustomKhachHangDetail) {
                // Lấy ID của nhân viên từ UserDetails
                return ((CustomKhachHangDetail) principal).getKhachHangId();
            }
        }


        return null;
    }

    public Long getCurrentNhanVienId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        if (authentication != null && authentication.isAuthenticated()) {

            Object principal = authentication.getPrincipal();


            if (principal instanceof CustomNhanVienDetail) {

                return ((CustomNhanVienDetail) principal).getId();
            }
        }


        return null;
    }

    public String getCurrentNhanVienTen() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        if (authentication != null && authentication.isAuthenticated()) {

            Object principal = authentication.getPrincipal();


            if (principal instanceof CustomNhanVienDetail) {

                return ((CustomNhanVienDetail) principal).getTen();
            }
        }


        return null;
    }
}
