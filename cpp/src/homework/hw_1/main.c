//
// Nariman Safiulin (woofilee)
// File: main.c
// Created on: Feb 19, 2016
//

#include <stdio.h>
#include <stdlib.h>

int digits(int n)
{
    int d = 0;
    while (n > 0)
    {
        d++;
        n /= 10;
    }
    return d;
}

int merge(int v, int n)
{
    if (v < 1)
        return 1;
    if (v > n)
        return n;
    return v;
}

int max(int a, int b)
{
    return (a > b) ? a : b;
}

int main(int argc, char** argv)
{
    int n, i, j;
    scanf("%d", &n);

    if (n < 1)
        n = 0;

    int** table = (int**) malloc(n * sizeof(int*));
    for (i = 0; i < n; i++)
    {
        table[i] = (int*) malloc(n * sizeof(int));
        for (j = 0; j < n; j++)
        {
            table[i][j] = (i + 1) * (j + 1);
        }
    }

    int x1, y1, x2, y2;
    while(scanf("%d", &x1))
    {
        if (x1 == 0)
            break;

        scanf("%d %d %d", &y1, &x2, &y2);
        x1 = max(0, merge(x1, n) - 1); x2 = merge(x2, n);
        y1 = max(0, merge(y1, n) - 1); y2 = merge(y2, n);

        for (i = x1; i < x2; i++)
        {
            for (j = y1; j < y2; j++)
            {
                printf("%*d", digits(table[x2 - 1][j]) + 1, table[i][j]);
            }
            printf("\n");
        }
    }

    for (i = 0; i < n; i++)
    {
        free(table[i]);
    }
    free(table);
    return 0;
}
