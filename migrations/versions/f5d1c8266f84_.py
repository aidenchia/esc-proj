"""empty message

Revision ID: f5d1c8266f84
Revises: 0a23ec766e91
Create Date: 2019-03-03 21:18:45.957644

"""
from alembic import op
import sqlalchemy as sa


# revision identifiers, used by Alembic.
revision = 'f5d1c8266f84'
down_revision = '0a23ec766e91'
branch_labels = None
depends_on = None


def upgrade():
    # ### commands auto generated by Alembic - please adjust! ###
    op.add_column('users', sa.Column('term', sa.Integer(), nullable=True))
    # ### end Alembic commands ###


def downgrade():
    # ### commands auto generated by Alembic - please adjust! ###
    op.drop_column('users', 'term')
    # ### end Alembic commands ###