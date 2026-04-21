package returns.mingleday.global.constant;

public class MailMessageConstant {
    public static String VERIFY_CODE_TITLE() {
        return "MingleDay 서비스 인증 코드입니다.";
    }

    public static String VERIFY_CODE_CONTENT(String purposeText, String authCode) {
        return "<html><body style='margin: 0; padding: 0; background-color: #f4f7f9;'>" +
                "<table align='center' border='0' cellpadding='0' cellspacing='0' width='100%' style='max-width: 600px; background-color: #ffffff; margin: 20px auto; border-radius: 8px; overflow: hidden; box-shadow: 0 4px 10px rgba(0,0,0,0.1);'>" +
                "<tr><td style='padding: 40px 20px; text-align: center; background-color: #4A90E2;'>" +
                "<h1 style='color: #ffffff; margin: 0; font-size: 24px; font-weight: bold;'>MingleDay</h1>" +
                "</td></tr>" +
                "<tr><td style='padding: 40px 30px; text-align: center;'>" +
                "<h2 style='color: #333333; margin-bottom: 10px;'>인증 코드를 확인해주세요</h2>" +
                "<p style='color: #666666; font-size: 16px; margin-bottom: 30px;'>" + purposeText + "</p>" +
                "<div style='display: inline-block; padding: 15px 40px; background-color: #f0f4f8; border-radius: 4px; border: 1px dashed #4A90E2;'>" +
                "<span style='font-size: 32px; font-weight: bold; color: #4A90E2; letter-spacing: 5px;'>" + authCode + "</span>" +
                "</div>" +
                "<p style='color: #999999; font-size: 14px; margin-top: 30px;'>인증 코드는 발송 후 5분 동안 유효합니다.</p>" +
                "</td></tr>" +
                "<tr><td style='padding: 20px; background-color: #f9f9f9; text-align: center;'>" +
                "<p style='font-size: 12px; color: #aaaaaa; margin: 0;'>" +
                "본인이 요청하지 않은 경우, 고객센터로 문의해 주세요.<br>" +
                "&copy; 2026 MingleDay. All rights reserved." +
                "</p></td></tr>" +
                "</table>" +
                "</body></html>";
    }

    public static String INVITATION_TITLE(String name) {
        return "[MingleDay] " + name + "님이 당신을 새로운 밍글에 초대했습니다.";
    }

    public static String INVITATION_CONTENT(String name, String mingleName, String mingleDescription, String url) {
        return "<html><body>" +
                "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; border: 1px solid #eee; padding: 20px;'>" +
                "<h2 style='color: #4A90E2;'>MingleDay 초대장</h2>" +
                "<p>안녕하세요!</p>" +
                "<p><strong>" + name + "</strong>님께서 " +
                "<strong>'" + mingleName + "'</strong> 밍글에 당신을 초대하셨습니다.</p>" +
                "<p style='background-color: #f9f9f9; padding: 15px; border-radius: 5px;'>" +
                "밍글 설명: " + mingleDescription +
                "</p>" +
                "<p>함께 일정을 관리하고 소통하려면 아래의 버튼을 클릭하여 참여해 주세요!</p>" +
                "<div style='text-align: center; margin-top: 30px;'>" +
                "<a href='" + url + "' style='background-color: #4A90E2; color: white; padding: 12px 25px; text-decoration: none; border-radius: 5px; font-weight: bold;'>초대 수락하기</a>" +
                "</div>" +
                "<p style='margin-top: 30px; font-size: 0.8em; color: #888;'>" +
                "본 메일은 MingleDay 서비스의 초대 기능을 통해 발송되었습니다.<br>" +
                "만약 초대를 원치 않으시면 이 메일을 무시하셔도 됩니다." +
                "</p>" +
                "</div>" +
                "</body></html>";
    }
}
