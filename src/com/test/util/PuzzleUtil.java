package com.test.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PuzzleUtil {

    private static final Integer BLANK_VALUE = -1;

    /**
     * 生成随机的Item
     */
    public static List<Integer> generatePuzzle(int columnCount, int rowCount) {
        int size = columnCount * rowCount;
        List<Integer> list = new ArrayList<Integer>(size);
        for (int i = 0; i < size; i++) {
            list.add(i);
        }
        // 设置最后一个格子为空格
        int blankIndex = size - 1;
        list.set(blankIndex, BLANK_VALUE);
        Collections.shuffle(list);
        if (!canSolve(list, columnCount)) {

            int indexA = size - 1;
            int a = list.get(indexA);
            if (a == BLANK_VALUE) {
                indexA--;
                a = list.get(indexA);
            }

            int indexB = indexA - 1;
            int b = list.get(indexB);
            if (b == BLANK_VALUE) {
                indexB--;
                b = list.get(indexB);
            }

            list.set(indexA, b);
            list.set(indexB, a);
        }
        return list;
    }

    /**
     * 是否拼图成功
     *
     * @return 是否拼图成功
     */
    public static boolean isSuccess(List<Integer> data) {
        for (int i = 0; i < data.size(); i++) {
            int value = data.get(i);
            if (value != i && value != BLANK_VALUE) {
                return false;
            }
        }
        return true;
    }

    /**
     * 该数据是否有解
     *
     * @param data
     * @return 该数据是否有解
     */
    public static boolean canSolve(List<Integer> data, int columnCount) {

        int totalRow = data.size() / columnCount;
        int blankRow = (data.indexOf(BLANK_VALUE) + 1) / columnCount;

        // 可行性原则
        if (columnCount % 2 == 1) {
            return getInversions(data) % 2 == 0;
        } else {
            // 从底往上数,空格位于奇数行
            if ((totalRow - blankRow) % 2 == 0) {
                return getInversions(data) % 2 == 0;
            } else {
            // 从底往上数,空位位于偶数行
                return getInversions(data) % 2 == 1;
            }
        }
    }

    /**
     * 计算倒置和算法
     *
     * @param data
     * @return 该序列的倒置和
     */
    public static int getInversions(List<Integer> data) {
        int inversions = 0;
        for (int i = 0; i < data.size(); i++) {
            int index = data.get(i);
            int inversionCount = 0;
            for (int j = i + 1; j < data.size(); j++) {
                if (data.get(j) != BLANK_VALUE && data.get(j) < index) {
                    inversionCount++;
                }
            }
            inversions += inversionCount;
        }
        return inversions;
    }

    public static List<Integer> solvePuzzle(List<Integer> puzzle, int columnCount, int rawCount) {

        if (puzzle.size() != columnCount * rawCount) {
            return null;
        }

        List<Integer> answer = new ArrayList<Integer>();
        List<Integer> copy = new ArrayList<Integer>(puzzle);

        System.out.println("Original puzzle");
        printPuzzle(puzzle, columnCount, rawCount);

        int blankIndex = solveLeftTop(copy, answer, columnCount, rawCount);

        System.out.println("solveLeftTop");
        printPuzzle(puzzle, columnCount, rawCount);

        blankIndex = solveRightTop(copy, answer, blankIndex, columnCount, rawCount);

        System.out.println("solveRightTop");
        printPuzzle(puzzle, columnCount, rawCount);

        blankIndex = solveLeftBottom(copy, answer, blankIndex, columnCount, rawCount);

        System.out.println("solveLeftBottom");
        printPuzzle(puzzle, columnCount, rawCount);

        blankIndex = solveRightBottom(copy, answer, blankIndex, columnCount, rawCount);

        System.out.println("solveRightBottom");
        printPuzzle(puzzle, columnCount, rawCount);

        return answer;
    }


    public static void printPuzzle(List<Integer> puzzle, int columnCount, int rawCount) {

        for (int i = 0; i < rawCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                int index = i * columnCount + j;
                System.out.print(String.format("%3s  ", puzzle.get(index)));
            }
            System.out.println();
        }
    }

    private static int solveRightBottom(List<Integer> puzzle, List<Integer> answer,
            int blankIndex, int columnCount, int rawCount) {

        int targetIndex = columnCount * (rawCount - 2) + columnCount - 2;
        int currentIndex = puzzle.indexOf(targetIndex);
        if (targetIndex != currentIndex) {
            blankIndex = moveToTarget(puzzle, answer,
                    currentIndex, targetIndex, blankIndex, columnCount);
        }

        return blankIndex;
    }

    private static int solveLeftBottom(List<Integer> puzzle, List<Integer> answer,
            int blankIndex, int columnCount, int rawCount) {

        int top = rawCount - 2;
        int bottom = rawCount - 1;
        for (int i = 0; i < columnCount - 2; i++) {

            int topValue = i + columnCount * top;
            int bottomValue = i + columnCount * bottom;
            int topTargetIndex = topValue;
            int bottomTargetIndex = bottomValue;

            int curTopIndex = puzzle.indexOf(topValue);
            int curBottomIndex = puzzle.indexOf(bottomValue);

            if (topValue == curTopIndex && bottomValue == curBottomIndex) {
                continue;
            }

            if (topValue != curBottomIndex || bottomValue != curTopIndex) {

                blankIndex = moveToTarget(puzzle, answer, curTopIndex,
                        bottomTargetIndex, blankIndex, columnCount);
                curBottomIndex = puzzle.indexOf(bottomValue);

                if (curBottomIndex != topTargetIndex) {
                    blankIndex = moveToTarget(puzzle, answer, curBottomIndex,
                            bottomTargetIndex + 1, blankIndex, columnCount);
                    // move the right one, avoid affect the left one when move blank to circle
                    blankIndex = moveToTarget(puzzle, answer, bottomTargetIndex + 1,
                            bottomTargetIndex, blankIndex, columnCount);
                    continue;
                }
            }

            blankIndex = reverseTopBottom(puzzle, answer, blankIndex,
                    topTargetIndex, bottomTargetIndex, columnCount);
        }

        return blankIndex;
    }

    private static int reverseTopBottom(List<Integer> puzzle, List<Integer> answer,
            int blankIndex, int topIndex, int bottomIndex, int columnCount) {

        blankIndex = moveToTarget(puzzle, answer, topIndex,
                topIndex + columnCount + 1, blankIndex, columnCount);

        blankIndex = moveBlankAroundLeftTop(puzzle, answer, blankIndex,
                topIndex + 1, columnCount);

        blankIndex = moveToTarget(puzzle, answer, topIndex + columnCount + 1,
                topIndex + 2, blankIndex, columnCount);

        blankIndex = moveToTarget(puzzle, answer, topIndex + 2,
                topIndex + 2 + columnCount, blankIndex, columnCount);

        blankIndex = moveBlankAroundRightTop(puzzle, answer, blankIndex,
                topIndex + 1, columnCount);

        blankIndex = moveToTarget(puzzle, answer, topIndex + 2 + columnCount,
                bottomIndex, blankIndex, columnCount);

        return blankIndex;
    }

    private static int moveBlankAroundRightTop(List<Integer> puzzle, List<Integer> answer,
            int blankIndex, int centerIndex, int columnCount) {

        List<Integer> moveList = new ArrayList<Integer>();
        if (blankIndex != centerIndex + 1) {
            moveList.add(centerIndex + 1);
        }
        moveList.add(centerIndex - columnCount + 1);
        moveList.add(centerIndex - columnCount);
        moveList.add(centerIndex - columnCount - 1);
        moveList.add(centerIndex - 1);

        for (Integer nextIndex : moveList) {
            int moveValue = puzzle.get(blankIndex);
            puzzle.set(blankIndex, moveValue);
            puzzle.set(nextIndex, BLANK_VALUE);
            blankIndex = nextIndex;
        }

        return blankIndex;
    }

    private static int moveBlankAroundLeftTop(List<Integer> puzzle, List<Integer> answer,
            int blankIndex, int centerIndex, int columnCount) {

        List<Integer> moveList = new ArrayList<Integer>();
        if (blankIndex != centerIndex - 1) {
            moveList.add(centerIndex - 1);
        }
        moveList.add(centerIndex - columnCount - 1);
        moveList.add(centerIndex - columnCount);
        moveList.add(centerIndex - columnCount + 1);
        moveList.add(centerIndex + 1);
        moveList.add(centerIndex + columnCount + 1);

        for (Integer nextIndex : moveList) {
            int moveValue = puzzle.get(blankIndex);
            puzzle.set(blankIndex, moveValue);
            puzzle.set(nextIndex, BLANK_VALUE);
            blankIndex = nextIndex;
        }

        return blankIndex;
    }

    private static int solveRightTop(List<Integer> puzzle, List<Integer> answer,
            int blankIndex, int columnCount, int rawCount) {

        int left = columnCount - 2;
        int right = columnCount - 1;
        for (int i = 0; i < rawCount - 2; i++) {

            int leftValue = i * columnCount + left;
            int rightValue = i * columnCount + right;
            int leftTargetIndex = leftValue;
            int rightTargetIndex = rightValue;

            int curLeftIndex = puzzle.indexOf(leftValue);
            int curRightIndex = puzzle.indexOf(rightValue);

            if (leftValue == curLeftIndex && rightValue == curRightIndex) {
                continue;
            }

            if (leftValue != curRightIndex || rightValue != curLeftIndex) {

                blankIndex = moveToTarget(puzzle, answer, curLeftIndex,
                        rightTargetIndex, blankIndex, columnCount);
                curRightIndex = puzzle.indexOf(rightValue);

                if (curRightIndex != leftTargetIndex) {
                    blankIndex = moveToTarget(puzzle, answer, curRightIndex,
                            rightTargetIndex + columnCount, blankIndex, columnCount);
                    // move the right one, avoid affect the left one when move blank to circle
                    blankIndex = moveToTarget(puzzle, answer, rightTargetIndex + columnCount,
                            rightTargetIndex, blankIndex, columnCount);
                    continue;
                }
            }

            blankIndex = reverseleftRight(puzzle, answer, blankIndex,
                    leftTargetIndex, rightTargetIndex, columnCount);
        }

        return blankIndex;
    }

    private static int reverseleftRight(List<Integer> puzzle, List<Integer> answer,
            int blankIndex, int leftIndex, int rightIndex, int columnCount) {

        blankIndex = moveToTarget(puzzle, answer, leftIndex,
                leftIndex + columnCount + 1, blankIndex, columnCount);

        blankIndex = moveBlankAroundTopLeft(puzzle, answer, blankIndex,
                leftIndex + columnCount, columnCount);

        blankIndex = moveToTarget(puzzle, answer, leftIndex + columnCount + 1,
                leftIndex + columnCount * 2, blankIndex, columnCount);

        blankIndex = moveToTarget(puzzle, answer, leftIndex + columnCount * 2,
                leftIndex + columnCount * 2 + 1, blankIndex, columnCount);

        blankIndex = moveBlankAroundBottomLeft(puzzle, answer, blankIndex,
                leftIndex + columnCount, columnCount);

        blankIndex = moveToTarget(puzzle, answer, leftIndex + columnCount * 2 + 1,
                rightIndex, blankIndex, columnCount);

        return blankIndex;
    }

    private static int moveBlankAroundBottomLeft(List<Integer> puzzle, List<Integer> answer,
            int blankIndex, int centerIndex, int columnCount) {

        List<Integer> moveList = new ArrayList<Integer>();
        if (blankIndex != centerIndex + columnCount) {
            moveList.add(centerIndex + columnCount);
        }
        moveList.add(centerIndex + columnCount - 1);
        moveList.add(centerIndex - 1);
        moveList.add(centerIndex - columnCount - 1);
        moveList.add(centerIndex - columnCount);

        for (Integer nextIndex : moveList) {
            int moveValue = puzzle.get(blankIndex);
            puzzle.set(blankIndex, moveValue);
            puzzle.set(nextIndex, BLANK_VALUE);
            blankIndex = nextIndex;
        }

        return blankIndex;
    }

    private static int moveBlankAroundTopLeft(List<Integer> puzzle, List<Integer> answer,
            int blankIndex, int centerIndex, int columnCount) {

        List<Integer> moveList = new ArrayList<Integer>();
        if (blankIndex != centerIndex - columnCount) {
            moveList.add(centerIndex - columnCount);
        }
        moveList.add(centerIndex - columnCount - 1);
        moveList.add(centerIndex - 1);
        moveList.add(centerIndex + columnCount - 1);
        moveList.add(centerIndex + columnCount);
        moveList.add(centerIndex + columnCount + 1);

        for (Integer nextIndex : moveList) {
            int moveValue = puzzle.get(blankIndex);
            puzzle.set(blankIndex, moveValue);
            puzzle.set(nextIndex, BLANK_VALUE);
            blankIndex = nextIndex;
        }

        return blankIndex;
    }

    private static int solveLeftTop(List<Integer> puzzle,
            List<Integer> answer, int columnCount, int rawCount) {

        int blankIndex = puzzle.indexOf(BLANK_VALUE);

        for (int i = 0; i < rawCount - 2; i++) {
            for (int j = 0; j < columnCount - 2; j++) {
                int targetIndex = i * columnCount + j;
                int currentIndex = puzzle.indexOf(targetIndex);
                blankIndex = moveToTarget(puzzle, answer, currentIndex,
                        targetIndex, blankIndex, columnCount);

                printPuzzle(puzzle, columnCount, rawCount);
            }
        }

        return blankIndex;
    }

    private static int moveToTarget(List<Integer> puzzle, List<Integer> answer,
            int currentIndex, int targetIndex, int blankIndex,int columnCount) {

        List<int[]> circleList = buildCircle(puzzle,
                currentIndex, targetIndex, columnCount);

        if (circleList != null) {
            if (!circleList.isEmpty()) {
                blankIndex = moveBlankToCircle(puzzle, answer,
                        circleList, blankIndex, targetIndex, columnCount);
                blankIndex = moveBlankInCircle(puzzle, circleList, answer,
                        currentIndex, targetIndex, blankIndex, columnCount);
            }
        }

        return blankIndex;
    }

    private static int moveBlankToCircle(List<Integer> puzzle,
            List<Integer> answer, List<int[]> circleList,
            int blankIndex, int targetIndex, int columnCount) {

        int blankX = blankIndex % columnCount;
        int blankY = blankIndex / columnCount;
        int targetX = targetIndex % columnCount;
        int targetY = targetIndex / columnCount;

        int absOffsetSum = Integer.MAX_VALUE;

        Point[] targetBlankPoints = new Point[2];

        for (int[] values : circleList) {
            int index = values[0];
            if (index != targetIndex) {
                int x = index % columnCount;
                int y = index / columnCount;
                int sum = Math.abs(x - blankX) + Math.abs(y - blankY);
                if (sum < absOffsetSum) {
                    absOffsetSum = sum;
                    if (targetBlankPoints[0] == null) {
                        targetBlankPoints[0] = new Point();
                    }
                    targetBlankPoints[0].index = index;
                    targetBlankPoints[0].x = x;
                    targetBlankPoints[0].y = y;
                } else if (sum == absOffsetSum) {
                    if (targetBlankPoints[1] == null) {
                        targetBlankPoints[1] = new Point();
                    }
                    targetBlankPoints[1].index = index;
                    targetBlankPoints[1].x = x;
                    targetBlankPoints[1].y = y;
                }
            }
        }

        int offsetBlankToTarget = Math.abs(targetX - blankX) + Math.abs(targetY - blankY);
        Point targetBlankPoint = targetBlankPoints[0];

        if (absOffsetSum > offsetBlankToTarget) {
            // 3 point in one line
            if (targetBlankPoint.x == blankX && blankX == targetX
                    || targetBlankPoint.y == blankY && blankY == targetY) {
                // when come here, the next is exist and not in one line.
                targetBlankPoint = targetBlankPoints[1];
            }
        }

        int offsetX = targetBlankPoint.x - blankX;
        int offsetY = targetBlankPoint.y - blankY;
        if (targetBlankPoint.x == targetX) {
            answer.addAll(moveBlankVertical(puzzle, blankIndex, offsetX, columnCount));
            answer.addAll(moveBlankHorinzal(puzzle, blankIndex, offsetY));
        } else {
            answer.addAll(moveBlankVertical(puzzle, blankIndex, offsetY, columnCount));
            answer.addAll(moveBlankHorinzal(puzzle, blankIndex, offsetX));
        }

        return targetBlankPoint.index;
    }

    private static class Point {
        int index;
        int x;
        int y;
    }


    private static List<Integer> moveBlankHorinzal(List<Integer> puzzle,
            int blankIndex, int offset) {

        List<Integer> answer = new ArrayList<Integer>();
        if (offset > 0) {
            for (int i = 1; i <= offset; i++) {
                int nextBlankPozition = blankIndex + i;
                answer.add(nextBlankPozition);
                puzzle.set(i, puzzle.get(nextBlankPozition));
            }
        } else if (offset < 0) {
            for (int i = -1; i >= offset; i--) {
                int nextBlankPozition = blankIndex + i;
                answer.add(nextBlankPozition);
                puzzle.set(i, puzzle.get(nextBlankPozition));
            }
        }

        puzzle.set(blankIndex + offset, BLANK_VALUE);
        return answer;
    }

    private static List<Integer> moveBlankVertical(List<Integer> puzzle,
            int blankIndex, int offset, int columnCount) {

        List<Integer> answer = new ArrayList<Integer>();
        int currentPozition = blankIndex;
        if (offset > 0) {
            for (int i = 1; i <= offset; i++) {
                int nextBlankPozition = blankIndex + i * columnCount;
                answer.add(nextBlankPozition);
                puzzle.set(currentPozition, puzzle.get(nextBlankPozition));
                currentPozition = nextBlankPozition;
            }
        } else if (offset < 0) {
            for (int i = -1; i >= offset; i--) {
                int nextBlankPozition = blankIndex + i * columnCount;
                answer.add(nextBlankPozition);
                puzzle.set(currentPozition, puzzle.get(nextBlankPozition));
                currentPozition = nextBlankPozition;
            }
        }

        puzzle.set(currentPozition, BLANK_VALUE);
        return answer;
    }

    private static List<int[]> buildCircle(List<Integer> puzzle,
            int currentIndex, int targetIndex, int columnCount) {

        int targetX = targetIndex % columnCount;
        int targetY = targetIndex / columnCount;
        int currentX = currentIndex % columnCount;
        int currentY = currentIndex / columnCount;

        if (targetX == currentX) {
            if (targetY == currentY) {
                return new ArrayList<int[]>(0);
            } else if (targetY < currentY) {
                currentX++;
            } else {
                //  never come here
                return null;
            }
        }

        if (targetY == currentY) {
            if (targetX < currentX) {
                currentY++;
            } else {
                // never come here
                return null;
            }
        }

        List<int[]> circleList = new ArrayList<int[]>();

        if (targetX < currentX && targetY < currentY) {

            int left = Math.min(targetX, currentX);
            int right = Math.max(targetX, currentX);
            int top = Math.min(targetY, currentY);
            int bottom = Math.max(targetY, currentY);

            for (int i = left; i < right; i++) {
                int[] values = new int[2];
                int index = i + top * columnCount;
                values[1] = index;
                values[0] = puzzle.get(index);
                circleList.add(values);
            }

            for (int i = top ; i < bottom; i++) {
                int[] values = new int[2];
                int index = right + i * columnCount;
                values[1] = index;
                values[0] = puzzle.get(index);
                circleList.add(values);
            }

            for (int i = right; i > left; i--) {
                int[] values = new int[2];
                int index = i + bottom * columnCount;
                values[1] = index;
                values[0] = puzzle.get(index);
                circleList.add(values);
            }

            for (int i = bottom ; i > top; i--) {
                int[] values = new int[2];
                int index = left + i * columnCount;
                values[1] = index;
                values[0] = puzzle.get(index);
                circleList.add(values);
            }
        } else {

            int outRight = 0;
            int inRight = 0;
            int outBottom = 0;
            int inBottom = 0;
            int left = 0;
            int top = 0;

            if (targetX < currentX && targetY > currentY) {

                if (currentX < columnCount - 1) {
                    outRight = currentX + 1;
                    inRight = currentX;
                } else {
                    outRight = currentX;
                    inRight = currentX - 1;
                }

                outBottom = targetY;
                inBottom = targetY + 1;

                left = targetX;
                top = currentY;
            } else if (targetX > currentX && targetY < currentY) {

                inRight = targetX;
                outRight = targetX + 1;

                if (targetY < currentY - 1) {
                    outBottom = currentY;
                    inBottom = currentY - 1;
                } else {
                    outBottom = currentY + 1;
                    inBottom = currentY;
                }

                left = currentX;
                top = targetY;
            } else {
                // come here error !
                return null;
            }

            for (int i = left; i < inRight; i++) {
                int[] values = new int[2];
                int index = i + inBottom * columnCount;
                values[1] = index;
                values[0] = puzzle.get(index);
                circleList.add(values);
            }

            for (int i = inBottom; i >= top; i--) {
                int[] values = new int[2];
                int index = inRight + i * columnCount;
                values[1] = index;
                values[0] = puzzle.get(index);
                circleList.add(values);
            }

            for (int i = top; i > outBottom; i--) {
                int[] values = new int[2];
                int index = outRight + i * columnCount;
                values[1] = index;
                values[0] = puzzle.get(index);
                circleList.add(values);
            }

            for (int i = outRight; i <= left; i++) {
                int[] values = new int[2];
                int index = i + outBottom * columnCount;
                values[1] = index;
                values[0] = puzzle.get(index);
                circleList.add(values);
            }
        }

        return circleList;
    }

    private static int moveBlankInCircle(List<Integer> puzzle,
            List<int[]> circleList, List<Integer> answer,
           int currentIndex, int targetIndex, int blankIndex, int columnCount) {

        int startPosition = 0;
        int targetPosition = 0;
        int currentPostion = 0;
        int targetValue = 0;
        int count = 0;

        for (int i = 0; i < circleList.size(); i++) {
            if (circleList.get(i)[0] == blankIndex) {
                startPosition = i;
                if (count == 2) {
                    break;
                }
                count++;
            } else if (circleList.get(i)[0] == currentIndex) {
                currentPostion = i;
                targetValue = circleList.get(i)[1];
                if (count == 2) {
                    break;
                }
                count++;
            } else if (circleList.get(i)[0] == targetIndex) {
                targetPosition = i;
                if (count == 2) {
                    break;
                }
                count++;
            }
        }

        int half = circleList.size() / 2;
        int step = 1;
        if (targetPosition - currentPostion > half
                || currentPostion - targetPosition < half) {
            step = -1;
        }

        int position = startPosition;
        int nextPosition = 0;
        while (circleList.get(targetPosition)[1] != targetValue) {

            if ((position < circleList.size() - 1 && step > 0)
                    || (position > 1 && step < 0)) {
                nextPosition = position + step;
            } else if (step > 1) {
                nextPosition = 0;
            } else {
                nextPosition = circleList.size() - 1;
            }
            int[] currentValue = circleList.get(position);
            int[] nextValue = circleList.get(nextPosition);
            currentValue[1] = nextValue[1];
            nextValue[1] = BLANK_VALUE;
            blankIndex = nextValue[0];
            answer.add(blankIndex);
        }

        // apply move to puzzle
        for (int[] values : circleList) {
            puzzle.set(values[0], values[1]);
        }

        return blankIndex;
    }
}

