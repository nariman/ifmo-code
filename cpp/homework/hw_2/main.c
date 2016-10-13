//
// Nariman Safiulin (woofilee)
// File: main.c
// Created on: Feb 27, 2016
//

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

char *filename;
FILE *io;

// HELPERS

int max(int a, int b) {
    return (a > b) ? a : b;
}

char *read(FILE *stream) {
    int curr_size = 0, max_size = 0;
    char *token = malloc(0), c = (char) fgetc(stream);
    while (isspace(c) || c == EOF) {
        c = (char) fgetc(stream);
    }

    while (!isspace(c) && c != EOF) {
        if (curr_size > max_size - 2) {
            max_size = curr_size + (1 << 8);
            token = realloc(token, max_size * sizeof(char));
        }
        token[curr_size++] = c;
        c = (char) fgetc(stream);
    }

    token[curr_size] = '\0';
    return token;
}

void file_open_for_rewrite() {
    fflush(io);
    fclose(io);
    fopen(filename, "w");
}

void file_open_for_append() {
    fflush(io);
    fclose(io);
    fopen(filename, "a");
}

// CONTACT LIST AND METHODS FOR WORKING WITH LIST

typedef struct Contact {
    int id;
    char *name;
    char *number;
} Contact;

typedef enum ContactInfoType {
    NAME,
    NUMBER
} ContactInfoType;

typedef struct ContactList {
    int size;
    int cumulative;
    Contact *contacts;
} ContactList;

ContactList list;

void rewrite_list() {
    file_open_for_rewrite();
    for (int i = 0; i < list.size; i++) {
        fprintf(io, "%d %s %s\n", list.contacts[i].id, list.contacts[i].name, list.contacts[i].number);
    }
    file_open_for_append();
}

char *cl_clear_name(const char *name) {
    int size = 0;
    while (name[size++] != '\0');
    char *cleared = malloc(size * sizeof(char));

    while (size --> 0) {
        cleared[size] = ('A' <= name[size] && name[size] <= 'Z') ? (char) (name[size] - 'A' + 'a') : name[size];
    }
    return cleared;
}

char *cl_clear_number(const char *number) {
    int need_size = 0, curr = -1, size = 0;

    while (number[++curr] != '\0') {
        need_size += ('0' <= number[curr] && number[curr] <= '9') ? 1 : 0;
    }

    curr = 0;
    char *cleared = malloc((need_size + 1) * sizeof(char));

    while (need_size) {
        if ('0' <= number[curr] && number[curr] <= '9') {
            cleared[size++] = number[curr];
            need_size--;
        }
        curr++;
    }

    cleared[size] = '\0';
    return cleared;
}

int cl_check_name(const char *name) {
    int curr = -1;
    char *cleared = cl_clear_name(name);

    while (cleared[++curr] != '\0') {
        if (cleared[curr] < 'a' || cleared[curr] > 'z') {
//            printf("[DEBUG] Name \"%s\" checked, error, symbol \"%c\" not allowed\n", cleared, cleared[curr]);
            return 0;
        }
    }

//    printf("[DEBUG] Name \"%s\" checked, ok\n", cleared);
    return 1;
}

int cl_check_number(const char *number) {
    int curr = (number[0] == '+') ? 0 : -1, pair = 0, pair_opened = 0, prev_is_dash = 0, digits_exists = 0;

    while (number[++curr] != '\0') {
        if ((number[curr] < '0' || number[curr] > '9')
            && !((number[curr] == '(' && !pair && !pair_opened)
                 || (number[curr] == ')' && pair_opened)
                 || (number[curr] == '-' && !prev_is_dash))) {
//            printf("[DEBUG] Number \"%s\" checked, error, symbol \"%c\" not allowed\n", number, number[curr]);
            return 0;
        }

        if (number[curr] == '(') {
            pair_opened = 1;
        } else if (number[curr] == ')') {
            pair = 1;
            pair_opened = 0;
        } else if (number[curr] == '-') {
            prev_is_dash = 1;
        } else {
            prev_is_dash = 0;
        }

        digits_exists = 1;
    }

    if (!digits_exists) {
//        printf("[DEBUG] Number \"%s\" checked, error, digits not found\n", number);
        return 0;
    }

    if (pair_opened) {
//        printf("[DEBUG] Number \"%s\" checked, error, pair not closed\n", number);
        return 0;
    }

//    printf("[DEBUG] Number \"%s\" checked, ok\n", number);
    return 1;
}

int cl_add(char *name, char *number, int to_file) {
    if (!cl_check_name(name)) {
        if (to_file) {
            printf("[ERROR] [ADD] Name must contains only letters a-z, A-Z\n");
        }
        return 0;
    }

    if (!cl_check_number(number)) {
        if (to_file) {
            printf("[ERROR] [ADD] Number must contains only digits 0-9 and symbols +, -, (, )\n");
        }
        return 0;
    }

    list.contacts = realloc(list.contacts, (list.size + 1) * sizeof(Contact));
    list.contacts[list.size].id = list.cumulative++;
    list.contacts[list.size].name = name;
    list.contacts[list.size++].number = number;

    if (to_file) {
        fprintf(io, "%d %s %s\n", list.cumulative - 1, name, number);
        fflush(io);
    }
    return 1;
}

void cl_find(char *data) {
    if (!cl_check_name(data) && !cl_check_number(data)) {
        printf("[ERROR] [FIND] Name must contains only letters a-z, A-Z, "
                              "Number must contains only digits 0-9 and symbols +, -, (, )\n");
        return;
    }

    int curr = -1, found = 0;
    char *supposed_name = cl_clear_name(data), *supposed_number = cl_clear_number(data);

    while (++curr < list.size) {
        if (strstr(cl_clear_name(list.contacts[curr].name), supposed_name) ||
            !strcmp(cl_clear_number(list.contacts[curr].number), supposed_number)) {
            printf("%d %s %s\n", list.contacts[curr].id, list.contacts[curr].name, list.contacts[curr].number);
            found++;
        }
    }

    if (!found) {
        printf("[ERROR] Google found nothing, Yandex is going crazy\n");
    }
    free(supposed_name);
    free(supposed_number);
}

int cl_change(int id, ContactInfoType type, char *data) {
    if (type == NAME && !cl_check_name(data)) {
        printf("[ERROR] [CHANGE] Name must contains only letters a-z, A-Z\n");
        return 0;
    }

    if (type == NUMBER && !cl_check_number(data)) {
        printf("[ERROR] [CHANGE] Number must contains only digits 0-9 and symbols +, -, (, )\n");
        return 0;
    }

    int curr = 0, found = 0;

    while (curr < list.size) {
        if (list.contacts[curr].id == id) {
            found++;
            break;
        }
        curr++;
    }

    if (!found) {
        printf("Contact with this ID is missing\n");
    }

    if (type == NAME) {
        list.contacts[curr].name = data;
    } else {
        list.contacts[curr].number = data;
    }
    rewrite_list();
    return 1;
}

int cl_remove(int id) {
    int curr = 0, found = 0;

    while (curr < list.size) {
        if (list.contacts[curr].id == id) {
            found++;
            break;
        }
        curr++;
    }

    if (!found) {
        printf("[ERROR] Contact with this ID is missing\n");
        return 0;
    }

    free(list.contacts[curr].name);
    free(list.contacts[curr].number);
    for (int i = curr + 1; i < list.size; i++) {
        list.contacts[i - 1] = list.contacts[i];
    }

    list.contacts = realloc(list.contacts, --list.size * sizeof(Contact));
    rewrite_list();
    return 1;
}

// MAIN LOGIC

void init() {
    list.cumulative = 1;
    list.size = 0;
    list.contacts = malloc(0);
    int id, m = 0;

    io = fopen(filename, "a+");
    rewind(io);

    while (fscanf(io, "%d", &id) > 0) {
        char *name = read(io);
        char *number = read(io);
        if (cl_add(name, number, 0)) {
            list.contacts[list.size - 1].id = id;
            m = max(m, id);
        }
    }

    list.cumulative = ++m;
    rewrite_list();
    fprintf(io, "\n");
}

void deinit() {
    fclose(io);
    for (int i = 0; i < list.size; i++) {
        free(list.contacts[i].name);
        free(list.contacts[i].number);
    }
    free(list.contacts);
}

int main(int argc, char const *argv[]) {
    filename = (char *) argv[1];
    init();
    char *cmd = malloc(10 * sizeof(char));

    while (scanf("%s", cmd)) {
        if (!strcmp(cmd, "create")) {
            char *name = read(stdin);
            char *number = read(stdin);
            cl_add(name, number, 1);
        } else if (!strcmp(cmd, "find")) {
            cl_find(read(stdin));
        } else if (!strcmp(cmd, "change")) {
            int id;
            scanf("%d %s", &id, cmd);
            char *data = read(stdin);
            if (!strcmp(cmd, "name")) {
                cl_change(id, NAME, data);
            } else if (!strcmp(cmd, "number")) {
                cl_change(id, NUMBER, data);
            } else {
                printf("[ERROR] [CHANGE] Unsupported command\n");
            }
        } else if (!strcmp(cmd, "delete")) {
            int id;
            scanf("%d", &id);
            cl_remove(id);
        } else if (!strcmp(cmd, "exit")) {
            break;
        } else {
            printf("[ERROR] Unsupported command\n");
        }
        fflush(stdout);
    }

    free(cmd);
    deinit();

//    printf("Checking memory");
//    int d;
//    scanf("%d", &d);
    return 0;
}
