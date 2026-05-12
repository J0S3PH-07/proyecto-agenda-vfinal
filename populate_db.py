import psycopg2
import boto3

def get_password():
    ssm = boto3.client('ssm', region_name='us-east-1')
    response = ssm.get_parameter(Name='/restricted-ecs-project/db/password', WithDecryption=True)
    return response['Parameter']['Value']

try:
    password = get_password()
    conn = psycopg2.connect(
        host='restricted-ecs-project-db.ch7kyg0nypq3.us-east-1.rds.amazonaws.com',
        database='agenda_db',
        user='restricted_user',
        password=password
    )
    cur = conn.cursor()
    
    # Read schema.sql
    with open('schema.sql', 'r') as f:
        schema_sql = f.read()
    
    cur.execute(schema_sql)
    
    # Insert sample rooms
    rooms = [('Àgora', '#2563eb'), ('Ateca', '#16a34a'), ('Sala de Juntes', '#dc2626')]
    for name, color in rooms:
        cur.execute("INSERT INTO sala (nom, color) VALUES (%s, %s) ON CONFLICT DO NOTHING", (name, color))
    
    conn.commit()
    cur.close()
    conn.close()
    print("Database populated successfully")
except Exception as e:
    print(f"Error: {e}")
