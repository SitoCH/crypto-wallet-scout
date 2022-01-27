DELETE FROM BannedContract WHERE network = 'POLYGON' AND
                                 contractId IN ('0x9e2d266d6c90f6c0d80a88159b15958f7135b8af',
                                               '0x8a953cfe442c5e8855cc6c61b1293fa648bae472',
                                               '0x0b91b07beb67333225a5ba0259d55aee10e3a578');

INSERT INTO BannedContract (contractId, network) VALUES
     ('0x9e2d266d6c90f6c0d80a88159b15958f7135b8af', 'POLYGON'),
     ('0x8a953cfe442c5e8855cc6c61b1293fa648bae472', 'POLYGON'),
     ('0x0b91b07beb67333225a5ba0259d55aee10e3a578', 'POLYGON');