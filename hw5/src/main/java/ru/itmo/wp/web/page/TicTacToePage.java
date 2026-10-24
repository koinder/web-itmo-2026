package ru.itmo.wp.web.page;

import ru.itmo.wp.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@SuppressWarnings({"unused", "RedundantSuppression"})
public class TicTacToePage {
    private static final String STATE_ATTRIBUTE = "ticTacToeState";

    private void action(HttpServletRequest request, Map<String, Object> view) {
        view.put("state", getState(request.getSession()));
    }

    // action=onMove, параметр cell_<row><col>
    private void onMove(HttpServletRequest request) {
        State state = getState(request.getSession());
        for (String name : request.getParameterMap().keySet()) {
            if (name.matches("cell_[0-9][0-9]")) {
                state.makeMove(name.charAt(5) - '0', name.charAt(6) - '0');
            }
        }
        throw new RedirectException(request.getRequestURI());
    }

    // action=newGame
    private void newGame(HttpServletRequest request) {
        request.getSession().setAttribute(STATE_ATTRIBUTE, new State());
        throw new RedirectException(request.getRequestURI());
    }

    private State getState(HttpSession session) {
        State state = (State) session.getAttribute(STATE_ATTRIBUTE);
        if (state == null) {
            state = new State();
            session.setAttribute(STATE_ATTRIBUTE, state);
        }
        return state;
    }

    public enum Phase {
        RUNNING, WON_X, WON_O, DRAW
    }

    // Состояние игры 
    public static class State {
        private static final int SIZE = 3;

        private final String[][] cells = new String[SIZE][SIZE];   // null: клетка пустая, иначе "X" или "O"
        private boolean crossesMove = true;                        // первыми ходят крестики
        private Phase phase = Phase.RUNNING;
        private int moves = 0;

        public int getSize() {
            return SIZE;
        }

        public String[][] getCells() {
            return cells;
        }

        public boolean isCrossesMove() {
            return crossesMove;
        }

        public Phase getPhase() {
            return phase;
        }

        
        public synchronized void makeMove(int row, int col) { //чтобы быстрые нажатия не сломали
            if (phase != Phase.RUNNING || row >= SIZE || col >= SIZE || cells[row][col] != null) {
                return;
            }

            String mark = crossesMove ? "X" : "O";
            cells[row][col] = mark;
            moves++;

            if (isWin(row, col, mark)) {
                phase = crossesMove ? Phase.WON_X : Phase.WON_O;
            } else if (moves == SIZE * SIZE) {
                phase = Phase.DRAW;
            }
            crossesMove = !crossesMove;
        }

        private boolean isWin(int row, int col, String mark) {
            boolean rowWin = true;
            boolean colWin = true;
            boolean diagonalWin = true;
            boolean antiDiagonalWin = true;
            for (int i = 0; i < SIZE; i++) {
                rowWin &= mark.equals(cells[row][i]);
                colWin &= mark.equals(cells[i][col]);
                diagonalWin &= mark.equals(cells[i][i]);
                antiDiagonalWin &= mark.equals(cells[i][SIZE - 1 - i]);
            }
            return rowWin || colWin || diagonalWin || antiDiagonalWin;
        }
    }
}
