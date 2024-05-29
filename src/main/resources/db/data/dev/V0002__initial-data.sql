DELETE FROM public.users;
DELETE FROM role;
DELETE FROM users_roles;

INSERT INTO role (id, name) VALUES (1, 'BASIC');
INSERT INTO role (id, name) VALUES (2, 'MEDIUM');
INSERT INTO role (id, name) VALUES (3, 'PREMIUM');

-- This user is used in all tests that require a logged user
INSERT INTO public.users (id, username, password, active, locked) VALUES (999, 'hernan.gonzalez81@gmail.com', '{bcrypt}$2a$10$r3x3x1Cc7Qfi3EaNU9Jyluc3x3MrrrNGubdKjwSs9NkP35fILyU7q', true, false);
