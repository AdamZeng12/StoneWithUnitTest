package tests;

import Stone.Lexer;
import Stone.ParseException;
import Stone.Token;
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

class LexerTest {
    private static String regexPat
            = "\\s*((//.*)|([0-9]+)|(\"(\\\\\"|\\\\\\\\|\\\\n|[^\"])*\")"
            + "|[A-Z_a-z][A-Z_a-z0-9]*|==|<=|>=|&&|\\|\\||\\p{Punct})?";
    private Pattern pattern;
    private ArrayList<Token> queue;
    private boolean hasMore;
    private LineNumberReader reader;
    private Lexer lexer;

    @BeforeAll
    static void initAll() {
    }

    @BeforeEach
    void init() {
        InputStream inputStream = new InputStream() {
            @Override
            public int read() throws IOException {
                return 1;
            }
        };
        this.pattern = Pattern.compile(regexPat);
        this.queue = new ArrayList<>();
        this.hasMore = false;
        this.reader = new LineNumberReader(new InputStreamReader(inputStream));

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

    void testPeekWithILargerThanSizeOfQueueSuccess() {
        this.hasMore = true;
        this.reader.setLineNumber(3);
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
        assertEquals(idTokenTwo,actualToken);
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