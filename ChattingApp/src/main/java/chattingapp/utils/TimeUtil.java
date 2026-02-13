/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chattingapp.utils;
import java.time.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.time.temporal.WeekFields;
import java.util.Locale;

/**
 *
 * @author CP
 */
public class TimeUtil {
    public static String formatTime(LocalDateTime sentAt) {
        LocalDateTime now = LocalDateTime.now();
        LocalDate sentDate = sentAt.toLocalDate();
        LocalDate today = now.toLocalDate();

        // 1. Nếu cùng ngày -> HH:mm
        if (sentDate.equals(today)) {
            return sentAt.toLocalTime().toString().substring(0, 5);
        }

        // 2. Nếu cùng tuần -> Thứ
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        int sentWeek = sentDate.get(weekFields.weekOfWeekBasedYear());
        int nowWeek = today.get(weekFields.weekOfWeekBasedYear());

        if (sentDate.getYear() == today.getYear() && sentWeek == nowWeek) {
            DayOfWeek dayOfWeek = sentDate.getDayOfWeek();
            return dayOfWeek.getDisplayName(TextStyle.FULL, new Locale("vi", "VN"));
        }

        // 3. Nếu cùng năm -> dd/MM
        if (sentDate.getYear() == today.getYear()) {
            return String.format("%02d/%02d",
                    sentDate.getDayOfMonth(),
                    sentDate.getMonthValue());
        }

        // 4. Khác năm -> dd/MM/yyyy
        return String.format("%02d/%02d/%d",
                sentDate.getDayOfMonth(),
                sentDate.getMonthValue(),
                sentDate.getYear());
    }
}
