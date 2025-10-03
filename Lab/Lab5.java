import java.io.*;
import java.util.StringTokenizer;

class Song {
    int id;
    int duration;

    public Song() {}

    public Song(int id, int duration) {
        this.id = id;
        this.duration = duration;
    }
}

class NodeSong {
    Song song;
    NodeSong prev;
    NodeSong next;

    public NodeSong() {}

    public NodeSong(Song song) {
        this.song = song;
    }

    public NodeSong(Song song, NodeSong prev, NodeSong next) {
        this.song = song;
        this.prev = prev;
        this.next = next;
    }
}

class Playlist {
    NodeSong head;
    NodeSong tail;
    NodeSong current;
    int currentTime = 0;
    boolean isOnRepeat = false;
    boolean hasPrint = false;

    public Playlist() {}

    public void addSongToQueue(Song song) {
        if (head == null) {
            head = new NodeSong(song);
            tail = head;
            current = head;
            hasPrint = false;
        }
        else {
            tail.next = new NodeSong(song, tail, null);
            tail = tail.next;
        }
        updateHeadTail();
    }

    public void addNextSong(Song song) {
        if (head == null) {
            head = new NodeSong(song);
            tail = head;
            current = head;
            hasPrint = false;
        }
        else {
            NodeSong next = current.next;
            current.next = new NodeSong(song, current, next);
            next.prev = current.next;
            while (tail.next != head) { // Kalau di tail
                tail = tail.next;
            }
        }
        updateHeadTail();
    }

    public void deleteSong(int idSong) {
        NodeSong c = head;

        if (c == tail) {
            head = null;
            tail = null;
            current = null;
            currentTime = 0;
            return;
        }

        do {
            if (c.song.id == idSong) {
                remove(c);
                if (c == current) { // Pindah kalau ke-delete lagu yang lagi didengar
                    current = current.next;
                    currentTime = 0;
                }
                break;
            }
            c = c.next;
        } while(c != head);
        updateHeadTail();
    }

    public void playSong(int idSong) {
        NodeSong c = head;
        do {
            if (c.song.id == idSong) {
                current = c;
                currentTime = 0;
                break;
            }
            c = c.next;
        } while(c != head);
    }

    public void addPlaylist(Playlist playlist) {
        if (head == null) {
            head = playlist.head;
            tail = playlist.tail;
            current = head;
            hasPrint = false;
        }
        else {
            tail.next = playlist.head;
            playlist.head.prev = tail;
            tail = playlist.tail;
        }
        updateHeadTail();
    }

    public void updateHeadTail() {
        if (head != null && tail != null) {
            head.prev = tail;
            tail.next = head;
        }
    }

    public void remove(NodeSong node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }
}

public class Lab5 {
    private static InputReader in;
    private static PrintWriter out;
    private static Song[] songs;
    private static Playlist[] playlists;
    private static Playlist queue = new Playlist();
    private static int ID = 0;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // INPUT LAGU-LAGU
        int N = in.nextInt();
        songs = new Song[N];
        int duration;
        for (int i = 0; i < N; i++) {
            duration = in.nextInt();
            songs[i] = new Song(ID++, duration);
        }

        // INPUT PLAYLIST
        int M = in.nextInt();
        playlists = new Playlist[M];
        int Pi;
        for (int i=0 ; i < M ; i++) {
            playlists[i] = new Playlist();
            Pi = in.nextInt();
            for (int j=0 ; j < Pi ; j++) {
                playlists[i].addSongToQueue(songs[in.nextInt()]);
            }
        }

        int Q = in.nextInt();

        String input;
        while (Q > 0) {
            input = in.next();
            switch (input) {
                case "LISTEN": queryListen(); break;
                case "FORWARD": queryForward(); break;
                case "BACKWARD": queryBackward(); break;
                case "ADD_SONG_TO_QUEUE": queryAddSong(); break;
                case "ADD_NEXT_SONG": queryAddNext(); break;
                case "DELETE_SONG": queryDeleteSong(); break;
                case "PLAY_SONG": queryPlaySong(); break;
                case "ADD_PLAYLIST": queryAddPlaylist(); break;
                case "ON_REPEAT_SONG": queryOnRepeat(); break;
            }
            Q--;
        }
        
        out.close();
    }

    public static void queryListen() {
        int timestep = in.nextInt();

        while (true) {
            if (!queue.hasPrint) {
                out.println(queue.current.song.id); 
                queue.hasPrint = true;
            }
            if (timestep < (queue.current.song.duration - queue.currentTime)) {
                queue.currentTime += timestep;
                break;
            }
            timestep -= (queue.current.song.duration - queue.currentTime);
            queue.currentTime = 0;
            if (!queue.isOnRepeat) {
                queue.current = queue.current.next;
            }
            out.println(queue.current.song.id); 
        }
    }

    public static void queryForward() {
        queue.current = queue.current.next;
        queue.currentTime = 0;
        out.println(queue.current.song.id); 
    }
    
    public static void queryBackward() {
        queue.current = queue.current.prev;
        queue.currentTime = 0;
        out.println(queue.current.song.id); 
    }

    public static void queryAddSong() {
        int idSong = in.nextInt();
        Song song = songs[idSong]; 
        queue.addSongToQueue(song);
    }

    public static void queryAddNext() {
        int idSong = in.nextInt();
        Song song = songs[idSong]; 
        queue.addNextSong(song);
    }

    public static void queryDeleteSong() {
        int idSong = in.nextInt();
        NodeSong current = queue.current;

        queue.deleteSong(idSong);

        if (queue.current != current) { 
            if (queue.current != null) {
                out.println(queue.current.song.id);
            }
        }
    }

    public static void queryPlaySong() {
        int idSong = in.nextInt();
        queue.playSong(idSong);
        if (queue.current.song.id == idSong && queue.currentTime == 0) {
            out.println(queue.current.song.id); 
        }
    }

    public static void queryAddPlaylist() {
        int idPlaylist = in.nextInt();
        queue.addPlaylist(playlists[idPlaylist]);
    }

    public static void queryOnRepeat() {
        String input = in.next();
        switch (input) {
            case "ON": queue.isOnRepeat = true; break;
            case "OFF": queue.isOnRepeat = false; break;
        }
    }

    // taken from https://codeforces.com/submissions/Petr
    // together with PrintWriter, these input-output (IO) is much faster than the
    // usual Scanner(System.in) and System.out
    // please use these classes to avoid your fast algorithm gets Time Limit
    // Exceeded caused by slow input-output (IO)
    private static class InputReader {

        public BufferedReader reader;
        public StringTokenizer tokenizer;

        public InputReader(InputStream stream) {
            reader = new BufferedReader(new InputStreamReader(stream), 32768);
            tokenizer = null;
        }

        public String next() {
        while (tokenizer == null || !tokenizer.hasMoreTokens()) {
            try {
            tokenizer = new StringTokenizer(reader.readLine());
            } catch (IOException e) {
            throw new RuntimeException(e);
            }
        }
        return tokenizer.nextToken();
        }

        public char nextChar() {
            return next().charAt(0);
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }
    }
}