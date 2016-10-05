//
// Created by WooFi on 05.10.2015.
//

#include <bits/stdc++.h>

#define TASK "kth"
#define fi first
#define se second
#define pb push_back
#define pop pop_back

using namespace std;

ifstream in(TASK ".in");
ofstream out(TASK ".out");

int n, k, a, b, c;
int arr[(int)1e7 * 3 + 5];

// find kth (kitten) element
int kitten(int l, int r, int k)
{
    int i = l, j = r, pivot = arr[(l + r) / 2], t;

    while (i <= j)
    {
        while (arr[i] < pivot)
            i++;
        while (arr[j] > pivot)
            j--;
        if (i <= j)
        {
            t = arr[i];
            arr[i] = arr[j];
            arr[j] = t;
            i++;
            j--;
        }
    }

    if (l <= k && k <= j)
        return kitten(l, j, k);
    else if (i <= k && k <= r)
        return kitten(i, r, k);
    return arr[k];
}

int main()
{
    in >> n >> k >> a >> b >> c >> arr[0];

    if (n == 1)
    {
        out << arr[0];
        return 0;
    }

    in >> arr[1];

    if (n == 2)
    {
        out << ((k == 1) ? min(arr[0], arr[1]) : max(arr[0], arr[1]));
        return 0;
    }

    for (int i = 2; i < n; i++)
        arr[i] = a * arr[i - 2] + b * arr[i - 1] + c;

    out << kitten(0, n - 1, k - 1);
    return 0;
}