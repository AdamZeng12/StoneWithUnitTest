import Stone.Lexer.Lexer;
import Stone.exception.ParseException;
import Stone.Lexer.Token;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LexerTest {
    private static String regexPat
            = "\\s*((//.*)|([0-9]+)|(\"(\\\\\"|\\\\\\\\|\\\\n|[^\"])*\")"
            + "|[A-Z_a-z][A-Z_a-z0-9]*|==|<=|>=|&&|\\|\\||\\p{Punct})?";
    private Pattern pattern;
    private ArrayList<Token> queue;
    private boolean hasMore;
    private LineNumberReader reader;
    private Lexer lexer;

    @BeforeEach
    void setup() {
        InputStream inputStream = new InputStream() {
            @Override
            public int read() throws IOException {
                return 1;
            }
        };
        this.pattern = Pattern.compile(regexPat);
        this.queue = new ArrayList<>();
        this.hasMore = true;
        this.reader = mock(LineNumberReader.class);

        lexer = new Lexer(pattern, queue, hasMore, reader);
    }

    @Test
    void testReadSuccess() {
        this.queue.clear();
        Token token = new Lexer.IdToken(1, "1");
        this.queue.add(token);
        Token actualToken = new Lexer.IdToken(2,"2");
        try {
            actualToken = lexer.read();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        assertEquals(token, actualToken);
    }
    @Test
    void testReadEOF() {
        this.hasMore = false;
        Token expectedToken = Token.EOF;

        Token actualToken = new Lexer.IdToken(2,"2");
        try {
            actualToken = lexer.read();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        assertEquals(expectedToken, actualToken);
    }

    @Test
    void testPeekSuccess() {
        Token idTokenOne = new Lexer.IdToken(0,";");
        Token idTokenTwo = new Lexer.IdToken(0,">=");
        this.queue.add(idTokenOne);
        this.queue.add(idTokenTwo);
        Token actualToken = new Lexer.NumToken(1,2);
        try {
            actualToken = lexer.peek(1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assertEquals(idTokenTwo,actualToken);
    }

    @Test
    void testPeekWithILargerThanSizeOfQueueSuccess() {
        // return value
        String readerLineReturnValue = "<=";
        int readerGetLineNumberReturnValue = 10;
        
        // define the behavior of this.reader
        try {
            when(this.reader.readLine()).thenReturn(readerLineReturnValue);
        } catch (IOException e) {
            e.printStackTrace();
        }
        when(this.reader.getLineNumber()).thenReturn(readerGetLineNumberReturnValue);

        Token expectedToken = new Lexer.IdToken(10,"<=");
        Token idTokenOne = new Lexer.IdToken(0,";");
        Token idTokenTwo = new Lexer.IdToken(1,">=");
        this.queue.add(idTokenOne);
        this.queue.add(idTokenTwo);
        Token actualToken = new Lexer.NumToken(100000,2);
        try {
            actualToken = lexer.peek(2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assertEquals(true,tokenEquals(expectedToken,actualToken));

    }

    private boolean tokenEquals(Token expectedToken,Token actualToken) {
        return (expectedToken.isIdentifier() == actualToken.isIdentifier())  &&
                (expectedToken.getLineNumber() == actualToken.getLineNumber()) &&
                (expectedToken.getText().equals(actualToken.getText()));
    }
    @Test
    void testReadLine() {
    }

    @Test
    void testAddToken() {
    }

    @Test
    void testToStringLiteral() {
    }
}