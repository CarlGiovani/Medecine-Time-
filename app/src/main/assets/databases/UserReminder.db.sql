BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "Table_Prebuilt" (
	"ID"	INTEGER,
	"Medicine_Name"	TEXT,
	"Medicine_Description"	TEXT,
	"Interval"	TEXT,
	"Note"	TEXT,
	PRIMARY KEY("ID" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "Table_User" (
	"ID"	INTEGER,
	"Medicine_Name"	TEXT,
	"Medicine_Description"	TEXT,
	"Interval"	TEXT,
	"Note"	TEXT,
	PRIMARY KEY("ID" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "android_metadata" (
	"locale"	TEXT
);
INSERT INTO "Table_Prebuilt" VALUES (1,'carl','giovanni',NULL,'yes daddy');
INSERT INTO "Table_Prebuilt" VALUES (2,'angela','hillary','','ayaw kol');
INSERT INTO "Table_Prebuilt" VALUES (3,'josh','mojica','','wag kang tamad!');
INSERT INTO "Table_Prebuilt" VALUES (4,'domenic','bussin','','cussin');
INSERT INTO "Table_User" VALUES (1,'help','help me','1','help me tasukete');
INSERT INTO "Table_User" VALUES (2,'yes','mommy','1','oni');
INSERT INTO "android_metadata" VALUES ('en_US');
COMMIT;
