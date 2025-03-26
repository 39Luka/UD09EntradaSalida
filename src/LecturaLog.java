public class LecturaLog {
    static final String FICHERO_LOG = "Linux.2k.log";
    String regex = "(?<fecha>[A-Z][a-z]{2} \\d{2} \\d{2}:\\d{2}:\\d{2}).*(?<sshd>sshd).*(?<rhost>rhost=.*) .*";
}
