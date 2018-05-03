import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

/**
 * �ʼ�������
 * java ʵ���ʼ��ķ��ͣ� ���ͼ��฽��
 * @author zhuxiongxian
 * @version 1.0
 * @created at 2016��10��8�� ����3:52:11
 */
public class EmailManager {

    public static String username = "antsitya@163.com"; // ��������(from����)
    public static String password = "shouquanma163"; // ��������
    public static String senderNick = "���ϿƼ�";   // �������ǳ�

    private Properties props; // ϵͳ���� 
    private Session session; // �ʼ��Ự���� 
    private MimeMessage mimeMsg; // MIME�ʼ����� 
    private Multipart mp;   // Multipart����,�ʼ�����,����,���������ݾ���ӵ����к�������MimeMessage���� 

    private static EmailManager instance = null; 

    public EmailManager() {
        props = System.getProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", "smtp.163.com");
        props.put("mail.smtp.port", "25");
        props.put("username", username);
        props.put("password", password);
        // �����Ự
        session = Session.getDefaultInstance(props);
        session.setDebug(false);
    }

    public static EmailManager getInstance() {
        if (instance == null) {
            instance = new EmailManager();
        }
        return instance; 
    }

    /**
     * �����ʼ�
     * @param from ������
     * @param to �ռ���
     * @param copyto ����
     * @param subject ����
     * @param content ����
     * @param fileList �����б�
     * @return
     */
    public boolean sendMail(String from, String[] to, String[] copyto, String subject, String content, String[] fileList) {
        boolean success = true;
        try {
            mimeMsg = new MimeMessage(session);
            mp = new MimeMultipart(); 

            // �Զ��巢�����ǳ�
            String nick = "";
            try {
                nick = javax.mail.internet.MimeUtility.encodeText(senderNick);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            // ���÷�����
//          mimeMsg.setFrom(new InternetAddress(from));
            mimeMsg.setFrom(new InternetAddress(from, nick));
            // �����ռ���
            if (to != null && to.length > 0) {
                String toListStr = getMailList(to);
                mimeMsg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toListStr));
            }
            // ���ó�����
            if (copyto != null && copyto.length > 0) {
                String ccListStr = getMailList(copyto);
                mimeMsg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(ccListStr)); 
            }
            // ��������
            mimeMsg.setSubject(subject);
            // ��������
            BodyPart bp = new MimeBodyPart(); 
            bp.setContent(content, "text/html;charset=utf-8");
            mp.addBodyPart(bp);
            // ���ø���
            if (fileList != null && fileList.length > 0) {
                for (int i = 0; i < fileList.length; i++) {
                    bp = new MimeBodyPart();
                    FileDataSource fds = new FileDataSource(fileList[i]); 
                    bp.setDataHandler(new DataHandler(fds)); 
                    bp.setFileName(MimeUtility.encodeText(fds.getName(), "UTF-8", "B"));
                    mp.addBodyPart(bp); 
                }
            }
            mimeMsg.setContent(mp); 
            mimeMsg.saveChanges(); 
            // �����ʼ�
            if (props.get("mail.smtp.auth").equals("true")) {
                Transport transport = session.getTransport("smtp"); 
                transport.connect((String)props.get("smtp.163.com"),(String)props.get("username"), (String)props.get("password")); 
                transport.sendMessage(mimeMsg, mimeMsg.getAllRecipients());
                transport.close(); 
            } else {
                Transport.send(mimeMsg);
            }
            System.out.println("�ʼ����ͳɹ�");
        } catch (MessagingException e) {
            e.printStackTrace();
            success = false;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    /**
     * �����ʼ�
     * @param from ������
     * @param to �ռ���, ���Email��Ӣ�Ķ��ŷָ�
     * @param cc ����, ���Email��Ӣ�Ķ��ŷָ�
     * @param subject ����
     * @param content ����
     * @param fileList �����б�
     * @return
     */
    public boolean sendMail(String from, String to, String cc, String subject, String content, String[] fileList) {
        boolean success = true;
        try {
            mimeMsg = new MimeMessage(session);
            mp = new MimeMultipart(); 

            // �Զ��巢�����ǳ�
            String nick = "";
            try {
                nick = javax.mail.internet.MimeUtility.encodeText(senderNick);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            // ���÷�����
//          mimeMsg.setFrom(new InternetAddress(from));
            mimeMsg.setFrom(new InternetAddress(from, nick));
            // �����ռ���
            if (to != null && to.length() > 0) {
                mimeMsg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            }
            // ���ó�����
            if (cc != null && cc.length() > 0) {
                mimeMsg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc)); 
            }
            // ��������
            mimeMsg.setSubject(subject);
            // ��������
            BodyPart bp = new MimeBodyPart(); 
            bp.setContent(content, "text/html;charset=utf-8");
            mp.addBodyPart(bp);
            // ���ø���
            if (fileList != null && fileList.length > 0) {
                for (int i = 0; i < fileList.length; i++) {
                    bp = new MimeBodyPart();
                    FileDataSource fds = new FileDataSource(fileList[i]); 
                    bp.setDataHandler(new DataHandler(fds)); 
                    bp.setFileName(MimeUtility.encodeText(fds.getName(), "UTF-8", "B"));
                    mp.addBodyPart(bp); 
                }
            }
            mimeMsg.setContent(mp); 
            mimeMsg.saveChanges(); 
            // �����ʼ�
            if (props.get("mail.smtp.auth").equals("true")) {
                Transport transport = session.getTransport("smtp"); 
                transport.connect((String)props.get("smtp.163.com"), (String)props.get("username"), (String)props.get("password")); 
                transport.sendMessage(mimeMsg, mimeMsg.getAllRecipients());
                transport.close(); 
            } else {
                Transport.send(mimeMsg);
            }
            System.out.println("�ʼ����ͳɹ�");
        } catch (MessagingException e) {
            e.printStackTrace();
            success = false;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    public String getMailList(String[] mailArray) {
        StringBuffer toList = new StringBuffer();
        int length = mailArray.length;
        if (mailArray != null && length < 2) {
            toList.append(mailArray[0]);
        } else {
            for (int i = 0; i < length; i++) {
                toList.append(mailArray[i]);
                if (i != (length - 1)) {
                    toList.append(",");
                }

            }
        }
        return toList.toString();
    }

    public static void main(String[] args) {
        String from = username;
        String[] to = {"2811259714@qq.com"};
        String[] copyto = {"antsitya@qq.com"};
        String subject = "<h2>������</h2>";
        String content = "<strong>���ѽ��</strong>";
        String[] fileList = new String[1];
        fileList[0] = "g:/����.txt";
        EmailManager.getInstance().sendMail(from, to, copyto, subject, content, fileList);
    }
}