"""empty message

Revision ID: 0eb683b7f6ba
Revises: 8c46f536111b
Create Date: 2019-03-03 22:30:35.346965

"""
from alembic import op
import sqlalchemy as sa


# revision identifiers, used by Alembic.
revision = '0eb683b7f6ba'
down_revision = '8c46f536111b'
branch_labels = None
depends_on = None


def upgrade():
    # ### commands auto generated by Alembic - please adjust! ###
    op.drop_column('users', 'test')
    # ### end Alembic commands ###


def downgrade():
    # ### commands auto generated by Alembic - please adjust! ###
    op.add_column('users', sa.Column('test', sa.VARCHAR(), autoincrement=False, nullable=True))
    # ### end Alembic commands ###
