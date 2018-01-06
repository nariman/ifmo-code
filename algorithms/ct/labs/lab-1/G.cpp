/**
 * Nariman Safiulin (woofilee)
 * File: G.cpp
 */

#include <iostream>
#include <stdlib.h>
#include <fstream>
#include <string>
using namespace std;

int heapv[100000], heapn[100000];
int c = 0, k = 1;
int p[500000];

void swap(int u, int v)
{
    int tn, tv;
    int pt;
    pt = p[heapn[u]];
    tv = heapv[u];
    tn = heapn[u];
    p[heapn[u]] = p[heapn[v]];
    heapv[u] = heapv[v];
    heapn[u] = heapn[v];
    p[heapn[v]] = pt;
    heapv[v] = tv;
    heapn[v] = tn;
}

void sift(int av, int an)
{
    int i = p[an];
    while (i > 0 && heapv[(i - 1) / 2] > heapv[i])
    {
        swap(i, (i - 1) / 2);
        i = (i - 1) / 2;
    }
}

void add(int x)
{
    int i;
    heapv[c++] = x;
    heapn[c - 1] = k;
    p[k] = c - 1;
    i = c - 1;
    while (i > 0 && heapv[(i - 1) / 2] > heapv[i])
    {
        swap(i, (i - 1) / 2);
        i = (i - 1) / 2;
    }

}

int removeMin()
{
    int i, j;
    int sv, sn;
    sv = heapv[0];
    sn = heapn[0];
    swap(0, c - 1);
    c--;
    i = 0;
    while (i * 2 + 1 < c)
    {
        j = i * 2 + 1;
        if (j + 1 < c && heapv[j + 1] < heapv[j])
            j++;
        if (heapv[j] < heapv[i])
        {
            swap(i, j);
            i = j;
        }
        else
            break;
    }
    return sv;
}

int main(void)
{
    ifstream cin("priorityqueue.in");
    ofstream cout("priorityqueue.out");
    string st;
    char ch;
    int n, ord;
    cin >> st;
    while (!cin.eof())
    {
        if (st[0] == 'p')
        {
            cin >> n;
            add(n);
        }
        else
        if (st[0] == 'd')
        {
            cin >> ord;
            cin >> n;
            heapv[p[ord]] = n;
            sift(heapv[p[ord]], heapn[p[ord]]);
        }
        else
        if (st[0] == 'e')
        {
            if (c > 0)
            {
                n = removeMin();
                cout << n << "\n";
            }
            else
                cout << "*" << "\n";
        }
        k++;
        cin >> st;
    }
    if (st[0] == 'e')
    {
        if (c > 0)
            cout << removeMin() << "\n";
        else
            cout << "*" << "\n";
    }
    return 0;
}

/*struct Elem
{
	int val, num;
};

Elem heap[100000];
int c = 0, k = 1;
int p[500000];

void swap(int u, int v)
{
	Elem t;
	int pt;
	pt = p[heap[u].num];
	t.val = heap[u].val;
	t.num = heap[u].num;
	p[heap[u].num] = p[heap[v].num];
	heap[u].val = heap[v].val;
	heap[u].num = heap[v].num;
	p[heap[v].num] = pt;
	heap[v].val = t.val;
	heap[v].num = t.num;
}

void sift(Elem a)
{
	int i = p[a.num];
	while (i > 0 && heap[(i - 1) / 2].val > heap[i].val)
	{
		swap(i, (i - 1) / 2);
		i = (i - 1) / 2;
	}
}

void add(int x)
{
	int i;
	heap[c++].val = x;
	heap[c - 1].num = k;
	p[k] = c - 1;
	i = c - 1;
	while (i > 0 && heap[(i - 1) / 2].val > heap[i].val)
	{
		swap(i, (i - 1) / 2);
		i = (i - 1) / 2;
	}

}

int removeMin()
{
	int i, j;
	Elem s;
	s.val = heap[0].val;
	s.num = heap[0].num;
	swap(0, c - 1);
	c--;
	i = 0;
	while (i * 2 + 1 < c)
	{
		j = i * 2 + 1;
		if (j + 1 < c && heap[j + 1].val < heap[j].val)
			j++;
		if (heap[j].val < heap[i].val)
		{
			swap(i, j);
			i = j;
		}
		else
			break;
	}
	return s.val;
}

int main(void)
{
	ifstream cin("priorityqueue.in");
	ofstream cout("priorityqueue.out");
	string st;
	char ch;
	int n, ord;
	cin >> st;
	while (!cin.eof())
	{
		if (st[0] == 'p')
		{
			cin >> n;
			add(n);
		}
		else
			if (st[0] == 'd')
			{
				cin >> ord;
				cin >> n;
				heap[p[ord]].val = n;
				sift(heap[p[ord]]);
			}
			else
			{
				if (c > 0)
					cout << removeMin << "\n";
				else
					cout << "*" << "\n";
			}
		k++;
		cin >> st;
	}
	return 0;
}*/
