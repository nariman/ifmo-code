#include <stdio.h>
#include <algorithm>
#include <vector>
#include <iostream>
#include <string>

using namespace std;

int n = 0, i = 0, d = 0;
vector<pair<int, int>> a;
string s, s1;
int key, value;

void swap(int i, int j){
    int t;
    t = a[i].first; a[i].first = a[j].first; a[j].first = t;
    t = a[i].second; a[i].second = a[j].second; a[j].second = t;
}

void siftUp(int i){
    while (a[i] < a[(i - 1) / 2]) {
        swap(i, (i - 1) / 2);
        i = (i - 1) / 2;
    };
};

void add(pair<int, int> k){
    a.push_back(k);
    int i = a.size() - 1;
    siftUp(i);
};

void siftDown(int i){
    bool stop = false;
    while ((!stop) && (i * 2 - 1 < a.size())){
        stop = true;
        int left = i * 2 + 1;
        int right = i * 2 + 2;
        bool rex = (right < a.size());
        if ((rex) && (a[left].first < a[i].first) && (a[left].first <= a[right].first)){
            swap(i, left); left = i; stop = !stop;
        }
        else
        if ((rex) && (a[right].first < a[i].first) && (a[right].first < a[left].first)){
            swap(i, right); i = right; stop = !stop;
        };
        if ((!rex) && (a[left].first < a[i].first)){
            swap(i, left); stop = !stop;
        };
    };
};

int main(){
    freopen("priorityqueue.in", "r", stdin);
    freopen("priorityqueue.out", "w", stdout);

    pair <int, int> min;
    int count = 0; //номер операции
    getline(cin, s);
    while (s != ""){
        count++;

        if (s.substr(0, 4) == "push") {
            int i = stoi(s.substr(5, s.length() - 5));
            pair <int, int> pushed;
            pushed.first = i;
            pushed.second = count;
            add(pushed);
        };
        if (s.substr(0, 11) == "extract-min"){
            if (a.size() != 0) {
                printf("%i\n", a[0].first);
                swap(0, a.size() - 1);
                a.erase(a.begin() + a.size() - 1);
                int i = 0;
                int j;
                while ((2 * i + 1) < (a.size())){
                    int left = 2 * i + 1;
                    int right = 2 * i + 2;
                    j = left;
                    if ((right < a.size()) && (a[right].first < a[left].first)){
                        j = right;
                    }
                    if (a[i].first <= a[j].first){
                        break;
                    };
                    swap(i, j);
                    i = j;
                };
            }
            else {
                printf("*\n");
            };
        };

        if (s.substr(0, 12) == "decrease-key") {
            s1 = s.substr(13, s.length() - 13);
            int j = 0;
            while (s1[j] != ' ')
            {
                j++;
            };
            key = stoi(s1.substr(0, j));// из строки выделен ключ
            s1.erase(0, j + 1); // из строки уцб
            if (s1 != ""){
                value = stoi(s1);
            };
            j = 0;
            if (a.size() != 0) {
                while (a[j].second != key){
                    j++;
                };
                a[j].first = value;
                siftUp(j);
            };
        };
        getline(cin, s);
    };
};