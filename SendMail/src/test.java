
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Address; 
import javax.mail.Message; 
import javax.mail.MessagingException; 
import javax.mail.Session; 
import javax.mail.Transport; 
import javax.mail.internet.InternetAddress; 
import javax.mail.internet.MimeMessage; 
  
public class test { 
 
  /** 
   * @param args 
   * @throws MessagingException 
   * @throws UnsupportedEncodingException 
   * @param my.user ���ͷ�������
   * @param my.pw �����������Ȩ��
   * @param my.title ���ͷ����ǳ�
   * @param to ���շ�����
   */ 
  public static void main(String[] args) throws MessagingException, UnsupportedEncodingException { 
    MyEmail my=new MyEmail();
    String to="2811259714@qq.com";
    Properties props = new Properties();
    props.setProperty("mail.smtp.auth", "true");  
    props.setProperty("mail.transport.protocol", "smtp");  
    Session session = Session.getInstance(props);  
    session.setDebug(true);
    Message msg = new MimeMessage(session);  
    msg.setText("��������");  
    try {
        msg.setFrom(new InternetAddress("antsitya@163.com",javax.mail.internet.MimeUtility.encodeText(my.title)));
    } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }  
    msg.setSubject("���Ǳ���");
    Transport transport = session.getTransport();  
    transport.connect("smtp.163.com",25,my.user,my.pw);  
    transport.sendMessage(msg,  
            new Address[]{new InternetAddress(to)});  

    transport.close();  
  } 
 
} 