/*
 * Nariman Safiulin (woofilee)
 * File: E.cpp
 * Created on: Jan 12, 2016
 */

#include <bits/stdc++.h>

#define TASK "treeeg"

using namespace std;

ifstream in(TASK ".in");
ofstream out(TASK ".out");

#define pb push_back
#define N 300000
#define M 19

int n;

vector<pair<int, int>> graph[N];
int parent[N];
int labels[N];
bool centroids[N];
int sizes[N];
int distances[N][M];
multiset<int> nears[N];

void dfs(int u, int p) { // u is current node, p is parent node
    sizes[u] = 1;

    for (auto v : graph[u])
        if (v.first != p && !centroids[v.first]) {
            dfs(v.first, u);
            sizes[u] += sizes[v.first];
        }
}

int decompose(int u, int depth) {
    dfs(u, -1);
    int nodes = sizes[u] / 2; // number of nodes in current subtree / 2
    int c = u;
    int p = -1;

    while (true) {
        for (auto v : graph[c])
            if (v.first != p && !centroids[v.first] && sizes[v.first] > nodes) {
                p = c;
                c = v.first;
                goto outer;
            }

        break;
        outer:;
    }

    centroids[c] = true;
    labels[c] = depth;

    for (auto v : graph[c])
        if (!centroids[v.first])
            parent[decompose(v.first, depth + 1)] = c;

    return c;
}

void distancesP(int u, int p, int label, int d) {
    distances[u][label] = d;

    for (auto v : graph[u])
        if (v.first != p && labels[v.first] >= label)
            distancesP(v.first, u, label, d + v.second);
}

int main() {
    in >> n;

    for (int i = 0; i < n; i++) {
        parent[i] = -1;
        nears[i].insert((int) 1e9);
    }

    for (int i = 0; i < n - 1; i++) {
        int u, v, l; in >> u >> v >> l; u--; v--;
        graph[u].pb(make_pair(v, l)); graph[v].pb(make_pair(u, l));
    }

    decompose(0, 0);
    for(int i = 0; i < n; i++)
        distancesP(i, -1, labels[i], 0);

    // Adding 0
    int v = 0, label = labels[0];
    while (v != -1) {
        nears[v].insert(distances[0][label]);
        v = parent[v];
        label--;
    }

    int q; in >> q;

    while (q --> 0) {
        char type; in >> type;
        int u; in >> u; u--;
        int v = u, label = labels[u];

        switch (type) {
            case '+':
                while (v != -1) {
                    nears[v].insert(distances[u][label]);
                    v = parent[v];
                    label--;
                }
                break;
            case '-':
                while (v != -1) {
                    nears[v].erase(nears[v].find(distances[u][label]));
                    v = parent[v];
                    label--;
                }
                break;
            case '?':
                int ans = (int) 1e9;

                while (v != -1) {
                    ans = min(ans, *(nears[v].begin()) + distances[u][label]);
                    v = parent[v];
                    label--;
                }

                out << ans << endl;
                break;
        }
    }

    return 0;
}
