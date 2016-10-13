#include <bits/stdc++.h>

#define TASK "antiqs"
#define fi first
#define se second
#define pb push_back
#define pop pop_back

using namespace std;

ifstream in(TASK ".in");
ofstream out(TASK ".out");

int n, arr[(int) 1e5];

void testgen()
{
    if (n == 1)
    {
        arr[0] = 1;
        return;
    }
    if (n == 2)
    {
        arr[0] = 1;
        arr[1] = 2;
        return;
    }

    arr[0] = 1;
    arr[1] = 2;

    for (int i = 2; i <= n; i++)
    {
        arr[i - 1] = arr[(i - 1) >> 1];
        arr[(i - 1) >> 1] = i;
    }

    return;
}

int main()
{
    in >> n;
    testgen();
    for (int i = 0; i < n; i++)
        out << arr[i] << " ";
    return 0;
}