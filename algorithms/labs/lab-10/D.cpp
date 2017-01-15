/*
 * Nariman Safiulin (woofilee)
 * File: D.cpp
 * Created on: Jan 12, 2016
 */

#include <bits/stdc++.h>

#define TASK "lwdb"

using namespace std;

ifstream in(TASK ".in");
ofstream out(TASK ".out");

#define pb push_back
#define N 100000
#define Q 100001
#define M 19

int n;

vector<pair<int, int>> graph[N];
int parent[N];
int labels[N];
bool centroids[N];
int sizes[N];
int distances[N][M];
multiset<pair<int, int>> nears[N]; // first is distance, second is node number
set<pair<int, int>> color_queue[N];
int color_queue_match[Q];
int colors[N];

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

//    cout << "Reading... " << endl;

    color_queue_match[100000] = 0;

    for (int i = 0; i < n; i++) {
        parent[i] = -1;
//        nears[i].insert((int) 1e9);
        color_queue[i].insert(make_pair((int) 1e9 + 10, 100000));
    }

    for (int i = 0; i < n - 1; i++) {
        int u, v, l; in >> u >> v >> l; u--; v--;
        graph[u].pb(make_pair(v, l)); graph[v].pb(make_pair(u, l));
    }

//    cout << "Building... " << endl;

    decompose(0, 0);
//    cout << "Centroid is "  << decompose(0, 0)  + 1<< endl;
//
//    for (int i = 0; i < n; i++) {
//        cout << parent[i] << " ";
//    }
//
//    cout << endl;

//    cout << "Counting distances... " << endl;

    for(int i = 0; i < n; i++)
        distancesP(i, -1, labels[i], 0);

//    cout << "Processing nears... " << endl;

    for(int i = 0; i < n; i++) {
        int v = i, label = labels[i];
        while (v != -1) {
            nears[v].insert(make_pair(distances[i][label], i));
            v = parent[v];
            label--;
        }
    }

//    cout << "Processing queries... " << endl;

    int q; in >> q;

    while (q --> 0) {
        int type, u; in >> type >> u; u--;
        int v = u, label = labels[u];

//        cout << "Q is " << q << endl;

        switch (type) {
            case 1: {
                int d, c; in >> d >> c;
//                unordered_set<int> near;

                color_queue_match[q] = c;

//                cout << "Trying to color all nodes near " << u + 1 << " about " << d << " into " << c << endl;
//                cout << "Steps" << endl;

                while (v != -1) {
                    int dd = d - distances[u][label];
                    if (dd < 0)  {
                        v = parent[v];
                        label--;
                        continue;
                    }
//                    cout << " - " << "in the " << v + 1 << " distance to " << u + 1 << " is " << dd << endl;

//                    cout << " - " << "before" << endl;

                    auto to = color_queue[v].lower_bound(make_pair(dd, 100000));

//                    for (auto it = color_queue[v].begin(); it != to; it++) {
//                        cout << " - - " << "color of all that near " << (*it).first << " is " << color_queue_match[(*it).second] << endl;
//                    }

                    // Removes all old colors if nodes near
                    color_queue[v].erase(color_queue[v].begin(), to);

                    // And sets a new color for nodes near
                    color_queue[v].insert(make_pair(dd, q));

//                    cout << " - " << "after" << endl;
//
//                    for (auto it = color_queue[v].begin(); it != color_queue[v].lower_bound(make_pair(dd, 100000)); it++) {
//                        cout << " - - " << "color of all that near " << (*it).first << " is " << color_queue_match[(*it).second] << endl;
//                    }

//                    for (auto it = nears[v].begin(); it != nears[v].end() && (*it).first <= dd; it++) {
////                        cout << " - - " << (*i).second + 1 << " with distance to us = " << (*i).first << endl;
//                        colors[(*it).second] = c;
////                        near.insert((*it).second);
//                    }

//                    cout << " - " << "nodes far: " << endl;
//
//                    for (auto i = it; i != nears[v].end(); i++) {
//                        cout << " - - " << (*i).second + 1 << " with distance to us = " << (*i).first << endl;
//                    }
//
//                    cout << endl;

                    v = parent[v];
                    label--;
                }

//                cout << "Found nodes near: ";
//                for (auto e : near) {
////                    cout << e + 1 << " ";
//                    colors[e] = c;
//                }

//                cout << endl << endl;

                break;
            }
            case 2: {
                int ciq = 100000;
//                cout << "Getting color of " << u + 1 << endl;

                while (v != -1) {
//                    cout << "Distance from " << v + 1 << " to " << u + 1 << " is " << distances[u][label] << endl;

                    auto to = color_queue[v].lower_bound(make_pair(distances[u][label], -1));

                    for (auto it = to; it != color_queue[v].end(); it++) {
//                        cout << " - " << "Color " << color_queue_match[(*it).second] << " (op. " << (*it).second <<  ") can paint us, cuz distance of it is " << (*it).first << endl;
                        ciq = min(ciq, (*it).second);
                    }

                    v = parent[v];
                    label--;
                }

//                cout << "Selected color is " << color_queue_match[ciq] << endl;
//                cout << endl;

                out << color_queue_match[ciq] << endl;
                break;
            }
        }
    }

    return 0;
}
