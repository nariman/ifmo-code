#include <bits/stdc++.h>

using namespace std;

void merge_radix_sort(string *a, string *b, int l, int r, int m, int k)
{
    if (r - l == 0)
        return;
    merge_radix_sort(a, b, l, (l + r) / 2, m, k);
    merge_radix_sort(a, b, (l + r) / 2 + 1, r, m, k);

    int lt = l, rt = (l + r) / 2 + 1, i = l;
    while (lt <= (l + r) / 2 && rt <= r)
    {
        int e = 0;
        for (int j = m - k; j < m; j++)
        {
            if (a[lt][j] < a[rt][j])
            {
                e = -1;
                break;
            }
            else if (a[lt][j] > a[rt][j])
            {
                e = 1;
                break;
            }
        }
        if (e == -1 || e == 0)
        {
            b[i] = a[lt];
            lt++;
        }
        else
        {
            b[i] = a[rt];
            rt++;
        }
        i++;
    }
    while (lt <= (l + r) / 2)
    {
        b[i] = a[lt];
        lt++;
        i++;
    }
    while (rt <= r)
    {
        b[i] = a[rt];
        rt++;
        i++;
    }

    for (int i = l; i <= r; i++)
    {
        a[i] = b[i];
    }
    return;
}

int main()
{
    int n, m, k;
    string a[1005], b[1005];

    freopen("radixsort.in", "r", stdin);
    freopen("radixsort.out", "w", stdout);

    cin >> n >> m >> k;
    for (int i = 0; i < n; i++)
    {
        cin >> a[i];
    }

    merge_radix_sort(a, b, 0, n - 1, m, k);

    for (int i = 0; i < n; i++)
    {
        cout << a[i] << "\n";
    }
}
