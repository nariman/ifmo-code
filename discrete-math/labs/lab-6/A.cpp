/*
 * Nariman Safiulin (woofilee)
 * File: A.cpp
 * Created on: Dec 25, 2016
 */

#include <bits/stdc++.h>

#define TASK "rainbow"

using namespace std;

ifstream in(TASK ".in");
ofstream out(TASK ".ans");

#define pb push_back
#define N 100
#define M 5000

struct Edge {
    int u;
    int v;
    int color;
};

int n;
int m;

vector<Edge> edges;
vector<int> graph[M];

set<int> right_set;
set<int> result_set;

bool color[N];
bool used[M];
bool x1[M];
bool x2[M];

int parent[M];

int msu_rank[N];
int msu[N];

void reset() {
    for (int i = 0; i < n; msu[i] = i, i++);
}

int get(int v) {
    return (v == msu[v]) ? msu[v] : msu[v] = get(msu[v]);
}

void unite(int u, int v) {
    int a = get(u);
    int b = get(v);

    if (a == b)
        return;

    if (msu_rank[a] < msu_rank[b])
        swap(a, b);

    if (msu_rank[a] == msu_rank[b])
        msu_rank[a]++;

    msu[b] = a;
}

int main() {
    in >> n >> m;

    for (int i = 0; i < m; right_set.insert(i++));

    for (int i = 0; i < m; i++) {
        int a, b, c;
        in >> a >> b >> c;
        edges.pb({ --a, --b, --c });
    }

    while(true) {
        for (int i = 0; i < m; graph[i++].clear());

        for (auto a : result_set) {
            reset();

            for (auto b : result_set)
                if (b == a)
                    continue;
                else
                    unite(edges[b].u, edges[b].v);

            for (auto c : right_set) {
                if (get(edges[c].u) != get(edges[c].v))
                    graph[a].pb(c);
                if (edges[c].color == edges[a].color || !color[edges[c].color])
                    graph[c].pb(a);
            }
        }

        reset();
        for (int i = 0; i < m; x1[i] = false, x2[i++] = false);

        for (auto l : result_set)
             unite(edges[l].u, edges[l].v);

        for (auto l : right_set) {
            if (get(edges[l].u) != get(edges[l].v))
                x1[l] = true;
            if (!color[edges[l].color])
                x2[l] = true;
        }

        queue<int> queue;

        for (int i = 0; i < m; i++) {
            used[i] = false;

            if (x1[i]) {
                parent[i] = -1;
                used[i] = true;
                queue.push(i);
            }
        }

        while (!queue.empty()) {
            int l = queue.front(); queue.pop();

            if (x2[l]) {
                while (l != -1) {
                    if (right_set.count(l) > 0) {
                        result_set.insert(l);
                        right_set.erase(l);
                        color[edges[l].color] = true;
                    } else {
                        result_set.erase(l);
                        right_set.insert(l);
                        color[edges[l].color] = false;
                    }

                    l = parent[l];
                }

                goto endloop;
            }

            for (auto k : graph[l])
                if (!used[k]) {
                    parent[k] = l;
                    used[k] = true;
                    queue.push(k);
                }
        }

        break;
        endloop:;
    }

    out << result_set.size() << "\n";
    for (auto l : result_set)
        out << l + 1 << " ";

    return 0;
}
