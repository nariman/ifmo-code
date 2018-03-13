/**
 * Nariman Safiulin (woofilee)
 * File: B.cpp
 * Created on: Oct 05, 2015
 */

#include <bits/stdc++.h>

#define TASK "priorityqueue"
#define fi first
#define se second
#define pb push_back

using namespace std;

ifstream in(TASK ".in");
ofstream out(TASK ".out");

string cmd;
int t1, t2, tp, cnt;
vector <pair <int, int>> heap;

void h_swap(int a, int b)
{
    int t = heap[a].fi;
    heap[a].fi = heap[b].fi;
    heap[b].fi = t;
    t = heap[a].se;
    heap[a].se = heap[b].se;
    heap[b].se = t;
    return;
}

int main()
{
    while (in >> cmd)
    {
        cnt++;
        //cout << cmd <<"\n";
        if (cmd[0] == 'p')
        {
            // H_ADD()
            in >> t1;
            heap.pb(pair <int, int>(t1, cnt));

            // H_SIFT_UP()
            int i = heap.size() - 1;
            while (heap[i].fi < heap[(i - 1) / 2].fi)
            {
                h_swap(i, (i - 1) / 2);
                i = (i - 1) / 2;
            }
        }
        else if (cmd[0] == 'e')
        {
            if (heap.size() == 0)
                out << "*\n";
            else
            {
                out << heap[0].fi << "\n";
                // H_MIN()
                h_swap(0, heap.size() - 1);
                heap.erase(heap.begin() + heap.size() - 1);

                // H_SIFT_DOWN()
                int i = 0;
                while (i * 2 + 1 < heap.size())
                {
                    int j = i * 2 + 1;
                    if (j + 1 < heap.size() && heap[j + 1].fi < heap[j].fi)
                        j++;

                    if (heap[j].fi < heap[i].fi)
                    {
                        h_swap(i, j);
                        i = j;
                    }
                    else
                        break;
                }
            }
        }
        else if (cmd[0] == 'd')
        {
            in >> t1 >> t2;
            //cout << t1 << " " << t2 << "\n";
            for (int j = 0; j < heap.size(); j++)
            {
                if (heap[j].se == t1)
                {
                    heap[j].fi = t2;

                    // H_SIFT_UP()
                    int i = j;
                    while (heap[i].fi < heap[(i - 1) / 2].fi)
                    {
                        h_swap(i, (i - 1) / 2);
                        i = (i - 1) / 2;
                    }
                    break;
                }
            }
        }
    }

    return 0;
}