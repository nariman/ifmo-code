//
// Created by WooFi on 05.10.2015.
//

#include <bits/stdc++.h>

#define TASK "kth"
#define fi first
#define se second
#define pb push_back

using namespace std;

ifstream in(TASK ".in");
ofstream out(TASK ".out");

int n, k, a, b, c;
int arr[(int)1e7 * 3 + 5];

void kitten_swap(int i, int j)
{
    int t = arr[i];
    arr[i] = arr[j];
    arr[j] = t;
    return;
}

// find kth (kitten) element
int kitten(int l, int r, int k)
{
    //cout << l << " " << r << " " << k << "\n";
    if (r - l < 4)
    {
        sort(arr + l, arr + r);
        return l + k;
    }

    int m = (r - l + 1) / 5;
    for (int i = 0; i < m; i++)
    {
        sort(arr + l + i * 5, arr + l + i * 5 + 4);
        kitten_swap(l + i, l + i * 5 + 2);
    }

    int pivot = kitten(l, l + m - 1, m / 2);

    int i = l, j = r;
    while (i <= j)
    {
        while (arr[i] < arr[pivot])
            i++;
        while (arr[j] > arr[pivot])
            j--;
        if (i <= j)
        {
            if (i == pivot)
                pivot = j;
            else if (j == pivot)
                pivot = i;
            kitten_swap(i, j);
            i++;
            j--;
        }
    }

    int offset = 1;
    i = pivot + 1;
    while (i <= r && arr[i] == arr[pivot])
    {
        offset++;
        i++;
    }

    if (k == pivot)
        return pivot;
    else if (k < pivot)
        return kitten(l, pivot - 1, k);
    else
        return kitten(pivot + offset, r, k - pivot - offset);
}

int main()
{
    in >> n >> k >> a >> b >> c >> arr[0] >> arr[1];
    for (int i = 2; i < n; i++)
        arr[i] = a * arr[i - 2] + b * arr[i - 1] + c;

    /*
    for (int i = 0; i < n; i++)
        cout << arr[i] << " ";
    cout << "\n";
    */

    out << arr[kitten(0, n - 1, k - 1)];

    /*
    sort(arr, arr + n);

    for (int i = 0; i < n; i++)
        cout << arr[i] << " ";
    */
}